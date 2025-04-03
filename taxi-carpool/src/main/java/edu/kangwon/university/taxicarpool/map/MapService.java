package edu.kangwon.university.taxicarpool.map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kangwon.university.taxicarpool.map.exception.KakaoApiParseException;
import edu.kangwon.university.taxicarpool.map.exception.NoSearchResultException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class MapService {

    private final RestTemplate restTemplate;

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    public MapService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public MapSearchResponseDTO search(String keyword, double x, double y) {
        String url = UriComponentsBuilder.fromUriString(
                "https://dapi.kakao.com/v2/local/search/keyword.json")
            .queryParam("query", keyword)
            .queryParam("x", x)
            .queryParam("y", y)
            .queryParam("radius", 2000) // 일단 반경 2km로 고정
            .queryParam("page", 1)
            .queryParam("size", 15)
            .queryParam("sort", "accuracy")
            .build().toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            String.class
        );

        // JSON -> 객체 변환
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root;
        try {
            root = objectMapper.readTree(response.getBody());
            JsonNode documents = root.get("documents");

            List<MapPlaceDTO> places = new ArrayList<>();

            for (JsonNode doc : documents) {
                // 필수 필드가 하나라도 없으면 해당 객체 건너뛰기
                if (doc.get("place_name") == null ||
                    doc.get("road_address_name") == null ||
                    doc.get("x") == null ||
                    doc.get("y") == null) {
                    continue;
                }

                MapPlaceDTO place = new MapPlaceDTO(
                    doc.get("place_name").asText(),
                    doc.get("road_address_name").asText(),
                    doc.get("x").asText(),
                    doc.get("y").asText()
                );
                places.add(place);
            }

            if (places.isEmpty()) {
                throw new NoSearchResultException("검색결과가 없습니다");
            }

            return new MapSearchResponseDTO(places);

        } catch (JsonProcessingException e) {
            throw new KakaoApiParseException("카카오 API 응답 파싱 실패");
        }
    }
}
