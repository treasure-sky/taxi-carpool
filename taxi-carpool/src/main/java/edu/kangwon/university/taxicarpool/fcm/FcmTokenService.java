package edu.kangwon.university.taxicarpool.fcm;

import com.google.firebase.ErrorCode;
import edu.kangwon.university.taxicarpool.fcm.dto.FcmTokenUpsertRequestDTO;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FcmTokenService {

    private final FcmTokenRepository fcmTokenRepository;

    /**
     * FCM 토큰 등록/갱신 (업서트) 동일한 사용자의 동일한 플랫폼 토큰이 있으면 갱신, 없으면 새로 생성
     */
    @Transactional
    public void upsertFcmToken(Long userId, FcmTokenUpsertRequestDTO requestDTO) {
        Optional<FcmTokenEntity> existingToken = fcmTokenRepository
            .findActiveTokenByUserIdAndPlatform(userId, requestDTO.getPlatform());

        if (existingToken.isPresent()) {
            // 기존 토큰이 있으면 업데이트
            FcmTokenEntity token = existingToken.get();
            if (!token.getFcmToken().equals(requestDTO.getFcmToken())) {
                // 토큰이 변경된 경우에만 업데이트
                token.updateToken(
                    requestDTO.getFcmToken(),
                    requestDTO.getDeviceId(),
                    requestDTO.getAppVersion(),
                    requestDTO.getBundleOrPackage()
                );
            }
        } else {
            // 새 토큰 생성
            FcmTokenEntity newToken = new FcmTokenEntity(
                userId,
                requestDTO.getFcmToken(),
                requestDTO.getPlatform(),
                requestDTO.getDeviceId(),
                requestDTO.getAppVersion(),
                requestDTO.getBundleOrPackage()
            );
            fcmTokenRepository.save(newToken);
        }
    }

    /**
     * 특정 FCM 토큰 폐기
     */
    @Transactional
    public void revokeTokenByFcmToken(String fcmToken) {
        fcmTokenRepository.revokeTokenByFcmToken(fcmToken);
    }

    /**
     * FCM 응답 오류에 따른 토큰 폐기 처리
     */
    @Transactional
    public void handleFcmError(String fcmToken, ErrorCode errorCode) {
        // UNREGISTERED, INVALID_REGISTRATION_TOKEN 등의 오류 시 토큰 폐기
        if (shouldRevokeToken(errorCode)) {
            revokeTokenByFcmToken(fcmToken);
        }
    }

    /**
     * FCM 오류 코드에 따라 토큰 폐기 여부 결정
     */
    private boolean shouldRevokeToken(ErrorCode errorCode) {
        String errorCodeStr = errorCode.name();
        return "UNREGISTERED".equals(errorCodeStr) ||
            "INVALID_REGISTRATION_TOKEN".equals(errorCodeStr) ||
            "NOT_FOUND".equals(errorCodeStr);
    }
}
