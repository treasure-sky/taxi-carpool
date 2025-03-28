package edu.kangwon.university.taxicarpool.email;

import edu.kangwon.university.taxicarpool.email.exception.EmailSendFailedException;
import edu.kangwon.university.taxicarpool.email.exception.EmailVerificationNotFoundException;
import edu.kangwon.university.taxicarpool.email.exception.ExpiredVerificationCodeException;
import edu.kangwon.university.taxicarpool.email.exception.InvalidVerificationCodeException;
import edu.kangwon.university.taxicarpool.member.MemberRepository;
import edu.kangwon.university.taxicarpool.member.exception.DuplicatedEmailException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailVerificationService {

    @Value("${email.address}")
    private String emailAddress;

    @Value("${email.name}")
    private String emailName;

    private final MemberRepository memberRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailVerificationService(MemberRepository memberRepository,
        EmailVerificationRepository emailVerificationRepository,
        JavaMailSender javaMailSender) {
        this.memberRepository = memberRepository;
        this.emailVerificationRepository = emailVerificationRepository;
        this.javaMailSender = javaMailSender;
    }

    public void sendCode(String email) throws Exception {

        // 이미 가입된 사용자가 이메일을 사용하고 있으면 예외 처리
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicatedEmailException("이미 사용 중인 이메일입니다: " + email);
        }

        String code = generateCode();

        // 인증 코드가 10분 후 만료되게 설정
        EmailVerificationEntity entity = new EmailVerificationEntity(email, code,
            LocalDateTime.now().plusMinutes(10));

        // 이미 인증코드를 받은 이력이 있으면 해당 데이터 삭제
        emailVerificationRepository.findByEmail(email).ifPresent(
            emailVerificationRepository::delete);

        emailVerificationRepository.save(entity);

        try {
            sendEmail(email, code);
        } catch (Exception e) {
            // 이메일 전송 실패 시 저장된 인증 코드 삭제
            emailVerificationRepository.delete(entity);
            // 이메일 전송 실패시 예외 처리
            throw new EmailSendFailedException("이메일 전송에 실패하였습니다.");
        }

    }

    /**
     * 인증코드 검증
     *
     * @param email 사용자 이메일
     * @param code  사용자가 입력한 코드
     * @return 인증 되었는지 여부
     */
    public void verifyCode(String email, String code) {

        // 1. DB에서 해당 email 조회
        EmailVerificationEntity entity = emailVerificationRepository.findByEmail(email)
            .orElseThrow(() -> new EmailVerificationNotFoundException("인증 정보를 찾을 수 없습니다."));

        // 2. 코드가 동일한지 체크
        if (!entity.getVerificationCode().equals(code)) {
            throw new InvalidVerificationCodeException("인증 코드가 일치하지 않습니다.");
        }

        // 3. 코드가 만료되었는지 체크
        if (entity.isExpired()) {
            throw new ExpiredVerificationCodeException("인증 코드가 만료되었습니다.");
        }

        // 4. 인증되었다고 저장
        entity.setVerified(true);
        emailVerificationRepository.save(entity);

    }

    /**
     * 이메일이 인증되었는지 여부 체크
     *
     * @param email 확인할 이메일
     * @return 이메일이 인증되었는지 여부
     */
    public boolean isEmailVerified(String email) {
        EmailVerificationEntity entity = emailVerificationRepository.findByEmail(email)
            .orElseThrow(() -> new EmailVerificationNotFoundException("인증 정보를 찾을 수 없습니다."));
        return entity.isVerified();
    }

    private String generateCode() {
        Random random = new SecureRandom();
        int num = random.nextInt(900000) + 100000; // 100000 ~ 999999
        return String.valueOf(num);
    }

    private void sendEmail(String to, String code) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setFrom(new InternetAddress(emailAddress, emailName));
        helper.setTo(to);
        helper.setSubject("인증코드 안내");
        helper.setText("인증코드: " + code);

        javaMailSender.send(message);
    }
}
