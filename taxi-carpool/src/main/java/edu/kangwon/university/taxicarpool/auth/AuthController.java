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
}
