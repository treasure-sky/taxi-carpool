package edu.kangwon.university.taxicarpool.auth.reset;

import edu.kangwon.university.taxicarpool.auth.JwtUtil;
import edu.kangwon.university.taxicarpool.auth.authException.TokenExpiredException;
import edu.kangwon.university.taxicarpool.auth.authException.TokenInvalidException;
import edu.kangwon.university.taxicarpool.email.exception.EmailSendFailedException;
import edu.kangwon.university.taxicarpool.member.MemberEntity;
import edu.kangwon.university.taxicarpool.member.MemberRepository;
import edu.kangwon.university.taxicarpool.member.MemberService;
import edu.kangwon.university.taxicarpool.member.exception.MemberNotFoundException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PasswordResetService {

    private final MemberService memberService;
    private final PasswordResetTokenRepository tokenRepository;
    private final JavaMailSender mailSender;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Value("${email.address}")
    private String emailAddress;

    @Value("${email.name}")
    private String emailName;

    // 프론트 리셋 페이지 베이스 URL
    @Value("${app.password-reset.base-url:http://localhost:3000/reset-password}")
    private String resetBaseUrl;

    // 1회용 토큰 유효시간(분)
    private static final long RESET_TOKEN_MINUTES = 10;

    public PasswordResetService(MemberService memberService,
        PasswordResetTokenRepository tokenRepository,
        JavaMailSender mailSender,
        JwtUtil jwtUtil,
        PasswordEncoder passwordEncoder) {
        this.memberService = memberService;
        this.tokenRepository = tokenRepository;
        this.mailSender = mailSender;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 비밀번호 재설정 링크 발송 (존재 여부는 항상 동일한 응답으로 은닉)
     */
    @Transactional
    public void sendResetLink(String email) {
        MemberEntity member = memberService.getMemberEntityByEmail(email);
        if (member == null) {
            // 예외처리는 getMemberEntityByEmail에서 구현중
            return;
        }

        // jti 생성 및 DB 저장(PasswordResetTokenEntity는 사실 상 jtiEntity)
        String jti = JwtUtil.newJti();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(RESET_TOKEN_MINUTES);
        PasswordResetTokenEntity tokenEntity = new PasswordResetTokenEntity(jti, member, expiresAt);
        tokenRepository.save(tokenEntity);

        // jti를 넣어서 JWT 생성 (subject=memberId, jti 포함, 짧은 만료)
        String jwt = jwtUtil.generatePasswordResetToken(
            member.getId(), jti, 1000L * 60 * RESET_TOKEN_MINUTES
        );

        // 링크 생성
        String link = resetBaseUrl + "?token=" + jwt;

        // 메일 발송
        sendResetEmail(email, link);
    }

    /**
     * 토큰 검증 후 비밀번호 변경(1회용)
     */
    @Transactional
    public void resetPassword(String token, String newPassword) {
        // 1) 토큰 기본 검증(서명/만료)
        var claims = jwtUtil.parseClaims(token);
        Long memberId = Long.valueOf(claims.getSubject());
        String jti = String.valueOf(claims.get("jti"));
        if (jti == null || jti.isBlank()) {
            throw new TokenInvalidException("유효하지 않은 토큰(jti 누락)");
        }

        // 2) DB에서 jti 조회 → 1회성/만료 검증 + 토큰 대상 일치 확인
        PasswordResetTokenEntity tokenEntity = tokenRepository.findByJti(jti)
            .orElseThrow(() -> new TokenInvalidException("유효하지 않은 토큰(jti 없음)"));

        if (tokenEntity.isUsed()) {
            throw new TokenInvalidException("이미 사용된 토큰입니다.");
        }
        if (tokenEntity.isExpired()) {
            throw new TokenExpiredException("토큰이 만료되었습니다. 다시 요청해 주세요.");
        }
        if (!tokenEntity.getMember().getId().equals(memberId)) {
            throw new TokenInvalidException("토큰 대상 정보가 일치하지 않습니다.");
        }

        // 3) 비밀번호 변경
        MemberEntity member = tokenEntity.getMember();
        member.setPassword(passwordEncoder.encode(newPassword));

        // 4) 토큰 사용 처리(1회용)
        tokenEntity.markUsed();

        // JPA flush는 @Transactional 종료 시점에 자동으로 반영되어서 save 불필요
    }

    private void sendResetEmail(String to, String resetLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setFrom(new InternetAddress(emailAddress, emailName));
            helper.setTo(to);
            helper.setSubject("[강원대 택시카풀] 비밀번호 재설정 안내");
            helper.setText(
                "아래 링크를 통해 비밀번호를 재설정하세요.\n\n" + resetLink +
                    "\n\n링크 유효시간: " + RESET_TOKEN_MINUTES + "분",
                false
            );
            mailSender.send(message);
        } catch (Exception e) {
            // 발송 실패 시에도 계정 존재 여부 노출을 피하기 위해 외부 응답은 동일하게 처리하지만,
            // 서버 로그/모니터링으로는 원인 파악 가능해야 함.
            throw new EmailSendFailedException("비밀번호 재설정 메일 전송 실패");
        }
    }
}
