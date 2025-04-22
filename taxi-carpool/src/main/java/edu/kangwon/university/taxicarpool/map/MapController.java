package edu.kangwon.university.taxicarpool.map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/map")
public class MapController {

    private final MapService mapService;

    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping("/search")
    public ResponseEntity<MapSearchResponseDTO> searchPlaces(
        @RequestParam String keyword,
        @RequestParam double x, // 경도
        @RequestParam double y  // 위도
    ) {
        MapSearchResponseDTO result = mapService.search(keyword, x, y);
        return ResponseEntity.ok(result);
    }

}
