package edu.kangwon.university.taxicarpool.email;

import edu.kangwon.university.taxicarpool.email.dto.EmailSendRequest;
import edu.kangwon.university.taxicarpool.email.dto.EmailVerifyRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    public EmailVerificationController(EmailVerificationService emailVerificationService) {
        this.emailVerificationService = emailVerificationService;
    }

    /**
     * 이메일 인증코드 발송
     */
    @PostMapping("/send")
    public ResponseEntity<String> sendCode(@RequestBody EmailSendRequest request) throws Exception {
        emailVerificationService.sendCode(request.getEmail());
        return ResponseEntity.ok("이메일 전송 완료");
    }

    /**
     * 인증코드 검증
     */
    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestBody EmailVerifyRequest request) {
        emailVerificationService.verifyCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok("인증 성공");
    }
}
