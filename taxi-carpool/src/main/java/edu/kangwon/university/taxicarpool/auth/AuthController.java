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

    @PostMapping("/login")
    public ResponseEntity<LoginDTO.LoginResponse> login(
        @Validated @RequestBody LoginDTO.LoginRequest loginRequest) {
        LoginDTO.LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginDTO.RefreshResponseDTO> refresh(@RequestBody LoginDTO.RefreshRequestDTO request) {
        LoginDTO.RefreshResponseDTO response = authService.refresh(request);
        return ResponseEntity.ok(response);
    }

    // 추가적으로 프론트측에서 엑세스 토큰도 제거해줘야함.
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutDTO.LogoutRequestDTO logoutRequest) {
        // 예: 클라이언트가 로그아웃 요청 시, email(또는 token으로 해도됨)을 request로 받자.
        authService.logout(logoutRequest.getEmail());
        return ResponseEntity.ok("로그아웃 완료 (Refresh 토큰 무효화)");
    }
}
