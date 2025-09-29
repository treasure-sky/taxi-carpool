package edu.kangwon.university.taxicarpool.fcm;

import edu.kangwon.university.taxicarpool.fcm.dto.FcmTokenUpsertRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FCM Token", description = "FCM 토큰 관리 API")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fcm")
public class FcmTokenController {

    private final FcmTokenService fcmTokenService;

    /**
     * FCM 토큰 등록/갱신 (업서트)
     */
    @Operation(
        summary = "FCM 토큰 등록/갱신",
        description = "클라이언트의 FCM 토큰을 등록하거나 갱신합니다. 동일한 플랫폼의 기존 토큰이 있으면 갱신하고, 없으면 새로 등록합니다."
    )
    @PostMapping("/token")
    public ResponseEntity<Void> upsertFcmToken(
        @Valid @RequestBody FcmTokenUpsertRequestDTO requestDTO
    ) {
        Long memberId = (Long) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();

        fcmTokenService.upsertFcmToken(memberId, requestDTO);

        return ResponseEntity.ok().build();
    }
    

}
