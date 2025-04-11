package edu.kangwon.university.taxicarpool.member;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    // 로그인, 비밀번호 찾기에 사용
    Optional<MemberEntity> findByEmail(String email);

    // 회원가입 시 이메일 중복 검증에 사용
    boolean existsByEmail(String email);

    // 회원가입, 회원정보 수정 시 닉네임 중복 검증에 사용
    boolean existsByNickname(String nickname);
}
