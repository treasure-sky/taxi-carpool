package edu.kangwon.university.taxicarpool.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpDTO.SignUpResponseDTO> signUp(
        @Validated @RequestBody SignUpDTO.SignUpRequestDTO signUpRequest) {

        SignUpDTO.SignUpResponseDTO response = authService.signUp(signUpRequest);
        return ResponseEntity.ok(response);
    }

    // 추후 로그인, 토큰 재발급 등 인증 관련 기능 추가 가능구현해야함.
}
