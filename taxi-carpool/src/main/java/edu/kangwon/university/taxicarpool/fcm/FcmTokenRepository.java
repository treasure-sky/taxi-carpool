package edu.kangwon.university.taxicarpool.fcm;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmTokenEntity, Long> {

    /**
     * 사용자 ID로 활성 FCM 토큰 목록 조회
     */
    @Query("SELECT f FROM fcm_token f WHERE f.userId = :userId AND f.revoked = false")
    List<FcmTokenEntity> findActiveTokensByUserId(@Param("userId") Long userId);

    /**
     * 사용자 ID와 플랫폼으로 활성 FCM 토큰 조회
     */
    @Query("SELECT f FROM fcm_token f WHERE f.userId = :userId AND f.platform = :platform AND f.revoked = false")
    Optional<FcmTokenEntity> findActiveTokenByUserIdAndPlatform(
        @Param("userId") Long userId,
        @Param("platform") Platform platform
    );

    /**
     * 사용자의 모든 토큰을 폐기 상태로 변경 (회원 탈퇴 시 사용)
     */
    @Modifying
    @Query("UPDATE fcm_token f SET f.revoked = true WHERE f.userId = :userId")
    void revokeAllTokensByUserId(@Param("userId") Long userId);

    /**
     * 특정 FCM 토큰을 폐기 상태로 변경
     */
    @Modifying
    @Query("UPDATE fcm_token f SET f.revoked = true WHERE f.fcmToken = :fcmToken")
    void revokeTokenByFcmToken(@Param("fcmToken") String fcmToken);
}
