package edu.kangwon.university.taxicarpool.party;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kangwon.university.taxicarpool.chatting.ChattingService;
import edu.kangwon.university.taxicarpool.chatting.MessageType;
import edu.kangwon.university.taxicarpool.member.MemberEntity;
import edu.kangwon.university.taxicarpool.member.MemberRepository;
import edu.kangwon.university.taxicarpool.member.exception.MemberNotFoundException;
import edu.kangwon.university.taxicarpool.party.dto.PartyCreateRequestDTO;
import edu.kangwon.university.taxicarpool.party.dto.PartyResponseDTO;
import edu.kangwon.university.taxicarpool.party.dto.PartyUpdateRequestDTO;
import edu.kangwon.university.taxicarpool.party.partyException.KakaoApiException;
import edu.kangwon.university.taxicarpool.party.partyException.MemberAlreadyInPartyException;
import edu.kangwon.university.taxicarpool.party.partyException.MemberNotInPartyException;
import edu.kangwon.university.taxicarpool.party.partyException.PartyAlreadyDeletedException;
import edu.kangwon.university.taxicarpool.party.partyException.PartyFullException;
import edu.kangwon.university.taxicarpool.party.partyException.PartyGetCustomException;
import edu.kangwon.university.taxicarpool.party.partyException.PartyInvalidMaxParticipantException;
import edu.kangwon.university.taxicarpool.party.partyException.PartyNotFoundException;
import edu.kangwon.university.taxicarpool.party.partyException.SavingsAlreadyCalculatedException;
import edu.kangwon.university.taxicarpool.party.partyException.UnauthorizedHostAccessException;
import java.io.IOException;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class PartyService {

    @Value("${kakaomobility.api.key}")
    private String kakaoMobilityApiKey;
    private final PartyRepository partyRepository;
    private final PartyMapper partyMapper;
    private final MemberRepository memberRepository;
    private final ChattingService chattingService;

    @Autowired
    PartyService(PartyRepository partyRepository,
        PartyMapper partyMapper,
        MemberRepository memberRepository, ChattingService chattingService
    ) {
        this.partyRepository = partyRepository;
        this.partyMapper = partyMapper;
        this.memberRepository = memberRepository;
        this.chattingService = chattingService;
    }

    public PartyResponseDTO getParty(Long partyId) {
        PartyEntity partyEntity = partyRepository.findByIdAndIsDeletedFalse(partyId)
            .orElseThrow(() -> new PartyNotFoundException("해당 파티가 존재하지 않습니다."));
        return partyMapper.convertToResponseDTO(partyEntity);
    }

    public Page<PartyResponseDTO> getPartyList(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.DESC, "createdAt"));
        Page<PartyEntity> partyEntities = partyRepository.findAllByIsDeletedFalse(pageable);
        return partyEntities.map(partyMapper::convertToResponseDTO);
    }

    @Transactional
    public Page<PartyResponseDTO> getCustomPartyList(
        Double userDepartureLng,
        Double userDepartureLat,
        Double userDestinationLng,
        Double userDestinationLat,
        LocalDateTime userDepartureTime,
        Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);

        if (userDepartureTime != null && userDepartureTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("출발 시간은 현재 시간보다 이후여야 합니다.");
        }

        // 각 그룹(출발지, 도착지, 출발시간)의 누락 여부 확인
        boolean missingDeparture = (userDepartureLng == null || userDepartureLat == null);
        boolean missingDestination = (userDestinationLng == null || userDestinationLat == null);
        boolean missingDepartureTime = (userDepartureTime == null);

        int missingCount = 0;
        if (missingDeparture) {
            missingCount++;
        }
        if (missingDestination) {
            missingCount++;
        }
        if (missingDepartureTime) {
            missingCount++;
        }

        // 2개 이상의 정보가 누락되었으면 예외 발생
        if (missingCount >= 2) {
            throw new PartyGetCustomException("출발지, 도착지, 출발시간에 대한 정보 중 2개 이상 넣어주세요!");
        }

        Page<PartyEntity> partyEntities = null;

        // 모든 정보가 있는 경우
        if (!missingDeparture && !missingDestination && !missingDepartureTime) {
            partyEntities = partyRepository.findCustomPartyList(
                userDepartureLng,
                userDepartureLat,
                userDestinationLng,
                userDestinationLat,
                userDepartureTime,
                pageable);

            // 출발지 정보가 누락된 경우
        } else if (missingDeparture) {
            partyEntities = partyRepository.findCustomPartyList(
                userDestinationLng,
                userDestinationLat,
                userDepartureTime,
                pageable);

            // 도착지 정보가 누락된 경우
        } else if (missingDestination) {
            partyEntities = partyRepository.findCustomPartyList(
                userDepartureLng,
                userDepartureLat,
                userDepartureTime,
                pageable);

            // 출발시간이 누락된 경우
        } else if (missingDepartureTime) {
            partyEntities = partyRepository.findCustomPartyList(
                userDepartureLng,
                userDepartureLat,
                userDestinationLng,
                userDestinationLat,
                pageable);

        }

        return partyEntities.map(partyMapper::convertToResponseDTO);

    }

    @Transactional
    public PartyResponseDTO createParty(PartyCreateRequestDTO createRequestDTO,
        Long CreatorMemberId) {

        PartyEntity partyEntity = partyMapper.convertToEntity(createRequestDTO);

        if (CreatorMemberId != null) {
            partyEntity.setHostMemberId(
                CreatorMemberId); // creatorMemberId(파티를 만든 멤버의 ID)를 HostMemberId로 설정
        } else {
            throw new IllegalArgumentException("파티방을 만든 멤버의 Id가 null임.");
        }

        // 처음 방 만든 멤버도 그 파티방의 멤버로 등록하는 것임.(이거 안 해놓으면 프론트한테 요청 2번 요청해야함.)
        MemberEntity member = memberRepository.findById(CreatorMemberId)
            .orElseThrow(() -> new MemberNotFoundException("파티방을 만든 멤버가 존재하지 않습니다."));
        partyEntity.getMemberEntities().add(member);

        partyEntity.setCurrentParticipantCount(1); // 방 만들고, 현재 인원 1명으로 설정

        PartyEntity savedPartyEntity = partyRepository.save(partyEntity);
        return partyMapper.convertToResponseDTO(savedPartyEntity);
    }

    @Transactional
    public PartyResponseDTO updateParty(Long partyId, Long memberId,
        PartyUpdateRequestDTO updateRequestDTO) {
        PartyEntity existingPartyEntity = partyRepository.findByIdAndIsDeletedFalse(partyId)
            .orElseThrow(() -> new PartyNotFoundException("해당 파티가 존재하지 않습니다."));

        if (!existingPartyEntity.getHostMemberId().equals(memberId)) {
            throw new UnauthorizedHostAccessException("호스트만 수정할 수 있습니다.");
        }

        // 현재 참여 인원보다 Max인원을 작게 설정하면 안 된다.
        if(existingPartyEntity.getCurrentParticipantCount() > updateRequestDTO.getMaxParticipantCount()) {
            throw new PartyInvalidMaxParticipantException("현재 참여 인원보다 작은 최대 인원으로 설정할 수 없습니다.");
        }

        partyMapper.convertToEntityByUpdate(existingPartyEntity, updateRequestDTO);

        PartyEntity savedPartyEntity = partyRepository.save(existingPartyEntity);
        return partyMapper.convertToResponseDTO(savedPartyEntity);
    }


    @Transactional
    public Map<String, Object> deleteParty(Long partyId, Long memberId) {
        PartyEntity partyEntity = partyRepository.findByIdAndIsDeletedFalse(partyId)
            .orElseThrow(() -> new PartyNotFoundException("해당 파티가 존재하지 않습니다."));
        if (!partyEntity.getHostMemberId().equals(memberId)) {
            throw new UnauthorizedHostAccessException("호스트만 삭제할 수 있습니다.");
        }

        partyEntity.setDeleted(true);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "파티가 성공적으로 삭제되었습니다.");
        response.put("deletedPartyId", partyId);

        return response;
    }

    // 멤버가 파티방의 멤버로 참가하는 로직의 메서드
    @Transactional
    public PartyResponseDTO joinParty(Long partyId, Long memberId) {
        PartyEntity party = partyRepository.findByIdAndIsDeletedFalse(partyId)
            .orElseThrow(() -> new PartyNotFoundException("해당 파티가 존재하지 않습니다."));
        if (party.isDeleted()) {
            throw new PartyAlreadyDeletedException("이미 삭제된 파티입니다.");
        }
        MemberEntity member = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException("해당 멤버가 존재하지 않습니다."));

        if (party.getMemberEntities().contains(member)) {
            throw new MemberAlreadyInPartyException("이미 이 파티에 참여한 멤버입니다.");
        }

        party.getMemberEntities().add(member);

        // 새로운 멤버가 파티 참가시, 현재인원 1명 추가
        int currentParticipantCount = party.getCurrentParticipantCount();
        if (currentParticipantCount < party.getMaxParticipantCount()) {
            currentParticipantCount += 1;
            party.setCurrentParticipantCount(currentParticipantCount);
        } else {
            throw new PartyFullException("현재 파티의 참여중인 인원수가 가득찼습니다.");
        }

        PartyEntity savedParty = partyRepository.save(party);
        chattingService.createSystemMessage(party, member, MessageType.ENTER);
        return partyMapper.convertToResponseDTO(savedParty);
    }

    @Transactional
    public PartyResponseDTO leaveParty(Long partyId, Long memberId) {
        PartyEntity party = partyRepository.findByIdAndIsDeletedFalse(partyId)
            .orElseThrow(() -> new PartyNotFoundException("해당 파티가 존재하지 않습니다."));
        MemberEntity member = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException("해당 멤버가 존재하지 않습니다."));

        if (!party.getMemberEntities().contains(member)) {
            throw new MemberNotInPartyException("이 멤버는 해당 파티에 속해있지 않습니다.");
        }

        // 호스트인 멤버가 파티를 떠나려고 할 때의 로직을 위한 isHostLeaving
        boolean isHostLeaving = (party.getHostMemberId() != null
            && party.getHostMemberId().equals(memberId));

        // 파티에서 멤버 제거
        party.getMemberEntities().remove(member);

        // 파티의 현재 인원 수 감소시키기
        int currentParticipantCount = party.getCurrentParticipantCount();
        if (currentParticipantCount > 1) {
            currentParticipantCount -= 1;
            party.setCurrentParticipantCount(currentParticipantCount);
        } else {
            // 앱의 플로우상 마지막으로 떠나는 멤버가 호스트일수밖에 없어서, 해당 코드가 필요하지 않을 것 같긴한데, 혹시 몰라서 일단 추가해둠.
            party.setDeleted(true);
            return partyMapper.convertToResponseDTO(party);
        }

        // 호스트인 멤버가 파티를 떠나려고 할 때의 로직.
        if (isHostLeaving) {
            List<MemberEntity> remaining = party.getMemberEntities();
            if (remaining.isEmpty()) {
                // 아무도 없으면 삭제 처리
                party.setDeleted(true);
                return partyMapper.convertToResponseDTO(party);
            } else {
                // 첫 멤버를 새 호스트로(호스트 제외하고 가장 빨리 들어온 멤버)
                MemberEntity nextHost = remaining.get(0);
                party.setHostMemberId(nextHost.getId());
            }
        }

        PartyEntity saved = partyRepository.save(party);
        chattingService.createSystemMessage(party, member, MessageType.LEAVE);
        return partyMapper.convertToResponseDTO(saved);
    }

    /**
     * 사용자가 속한 모든 파티를 조회합니다.
     *
     * @param memberId 사용자 ID
     * @return 사용자가 속한 모든 파티 목록
     */
    @Transactional(readOnly = true)
    public List<PartyResponseDTO> getMyParties(Long memberId) {

        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException("해당 멤버가 존재하지 않습니다: " + memberId);
        }

        List<PartyEntity> activeParties = partyRepository.findAllActivePartiesByMemberId(memberId);

        return activeParties.stream()
            .map(partyMapper::convertToResponseDTO)
            .toList();
    }

    @Transactional
    public Map<String, Object> calculateSavings(Long partyId, Long requesterId) {
        PartyEntity party = partyRepository.findByIdAndIsDeletedFalse(partyId)
            .orElseThrow(() -> new PartyNotFoundException("해당 파티가 존재하지 않습니다."));

        // 1) 요청자(호스트) 검증
        if (party.getHostMemberId() == null || !party.getHostMemberId().equals(requesterId)) {
            throw new UnauthorizedHostAccessException("호스트만 절감 금액 계산을 수행할 수 있습니다.");
        }

        if (party.isSavingsCalculated()) {
            throw new SavingsAlreadyCalculatedException("해당 파티("+ partyId +")는 이미 절감 계산이 완료되었습니다.");
        }

        // 2) 필수 파라미터 구성 (origin, destination, departure_time)
        if (party.getStartPlace() == null || party.getEndPlace() == null) {
            throw new IllegalArgumentException("출발/도착 좌표가 없습니다.");
        }

        double sx = party.getStartPlace().getX();
        double sy = party.getStartPlace().getY();
        double ex = party.getEndPlace().getX();
        double ey = party.getEndPlace().getY();
        validateCoordinates(sx, sy, "출발지");
        validateCoordinates(ex, ey, "도착지");

        String origin = sx + "," + sy;
        String destination = ex + "," + ey;

        // departure_time: YYYYMMDDHHMM (현재 이후가 요구되므로, 과거면 현재+2분로 보정)
        LocalDateTime depTime = party.getStartDateTime() != null
            ? party.getStartDateTime()
            : LocalDateTime.now().plusMinutes(2);
        if (!depTime.isAfter(LocalDateTime.now())) {
            depTime = LocalDateTime.now().plusMinutes(2);
        }
        String departureTime = depTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

        // 3) 카카오 모빌리티 API 호출
        String url = UriComponentsBuilder
            .fromHttpUrl("https://apis-navi.kakaomobility.com/v1/future/directions")
            .queryParam("origin", origin)
            .queryParam("destination", destination)
            .queryParam("departure_time", departureTime)
            .build(true) // 인코딩 보존
            .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoMobilityApiKey);
        headers.set("Content-Type", "application/json");

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response =
            restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        response = Optional.ofNullable(response)
            .filter(res -> res.getStatusCode().is2xxSuccessful())
            .orElseThrow(() -> new KakaoApiException("카카오 API 호출 실패: 성공(2xx) 응답이 아님. URL=" + url));

        if (response.getBody() == null || response.getBody().isBlank()) {
            throw new KakaoApiException("카카오 API 호출 실패: 응답 본문이 비어있음. URL=" + url);
        }

        // 4) taxi 요금 추출: routes[0].summary.fare.taxi
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root;
        try {
            root = mapper.readTree(response.getBody());
        } catch (IOException e) {
            throw new KakaoApiException("카카오 모빌리티 API 응답 파싱 실패: JSON 변환 불가", e);
        }

        JsonNode routes = Optional.ofNullable(root.path("routes"))
            .filter(JsonNode::isArray)
            .filter(r -> !r.isEmpty())
            .orElseThrow(() -> new KakaoApiException("카카오 API 응답 오류: 경로 정보가 비어있습니다."));

        JsonNode fare = Optional.ofNullable(routes.get(0).path("summary").path("fare"))
            .filter(f -> !f.isMissingNode())
            .orElseThrow(() -> new KakaoApiException("카카오 API 응답 오류: 요금 정보가 없습니다."));

        long totalTaxiFare = Optional.of(fare.path("taxi").asLong(0L))
            .filter(f -> f > 0L)
            .orElseThrow(() -> new KakaoApiException("카카오 API 응답 오류: 유효한 택시 요금을 가져오지 못했습니다."));


        // 5) 절감 금액 계산 및 멤버 누적 반영
        List<MemberEntity> members = party.getMemberEntities();
        int participants = (members != null) ? members.size() : 0;
        if (participants <= 0) {
            throw new MemberNotInPartyException("파티 참여 인원이 0명입니다.");
        }

        long eachShare = totalTaxiFare / participants;
        long savingPerMember = totalTaxiFare - eachShare; // "택시 비용 - (택시 비용 / 인원수)"

        // 누적 반영
        for (MemberEntity m : members) {
            m.addToTotalSavedAmount(savingPerMember);
        }
        memberRepository.saveAll(members);

        party.setSavingsCalculated(true);
        partyRepository.save(party);

        // 6) 응답 구성
        Map<String, Object> result = new HashMap<>();
        result.put("partyId", partyId);
        result.put("participants", participants);
        result.put("departure_time", departureTime);
        result.put("origin", origin);
        result.put("destination", destination);
        result.put("totalTaxiFare", totalTaxiFare);
        result.put("eachShare", eachShare);
        result.put("savingPerMember", savingPerMember);

        return result;
    }

    private void validateCoordinates(double x, double y, String label) {
        // 기본 좌표 유효성: 대략 한반도 범위 내로 제한
        // 경도: 124 ~ 132 (동서 방향)
        // 위도: 33 ~ 39 (남북 방향)
        if (x < 124 || x > 132) {
            throw new IllegalArgumentException(label + " 경도(x)가 한반도 범위를 벗어났습니다: x=" + x);
        }
        if (y < 33 || y > 39) {
            throw new IllegalArgumentException(label + " 위도(y)가 한반도 범위를 벗어났습니다: y=" + y);
        }
    }

}
