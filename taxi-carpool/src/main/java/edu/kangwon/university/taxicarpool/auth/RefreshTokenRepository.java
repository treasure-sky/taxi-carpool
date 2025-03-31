package edu.kangwon.university.taxicarpool.auth;

import edu.kangwon.university.taxicarpool.member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    // 멤버 기준으로 찾기 (멤버별 리프래쉬 토큰 1:1매핑)
    Optional<RefreshTokenEntity> findByMember(MemberEntity member);

    // 리프래쉬 토큰 문자열로 찾기
    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);
}
