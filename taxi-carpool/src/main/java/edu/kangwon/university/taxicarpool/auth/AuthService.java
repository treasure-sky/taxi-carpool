package edu.kangwon.university.taxicarpool.auth;

import edu.kangwon.university.taxicarpool.auth.authException.AuthenticationFailedException;
import edu.kangwon.university.taxicarpool.auth.authException.TokenExpiredException;
import edu.kangwon.university.taxicarpool.auth.authException.TokenInvalidException;
import edu.kangwon.university.taxicarpool.email.EmailVerificationService;
import edu.kangwon.university.taxicarpool.email.exception.EmailVerificationNotFoundException;
import edu.kangwon.university.taxicarpool.member.MemberEntity;
import edu.kangwon.university.taxicarpool.member.MemberService;
import edu.kangwon.university.taxicarpool.member.dto.MemberCreateDTO;
import edu.kangwon.university.taxicarpool.member.dto.MemberDetailDTO;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final MemberService memberService;
    private final EmailVerificationService emailVerificationService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public AuthService(MemberService memberService,
        EmailVerificationService emailVerificationService,
        PasswordEncoder passwordEncoder,
        JwtUtil jwtUtil,
        RefreshTokenRepository refreshTokenRepository) {
        this.memberService = memberService;
        this.emailVerificationService = emailVerificationService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // 회원가입
    public MemberDetailDTO signUp(MemberCreateDTO request) {

        // 이메일 인증여부 확인
        if(!emailVerificationService.isEmailVerified(request.getEmail())) {
            throw new EmailVerificationNotFoundException("이메일 인증을 먼저 해주세요.");
        }

        return memberService.createMember(request);
    }

    // 로그인
    public LoginDTO.LoginResponse login(LoginDTO.LoginRequest request) {

        MemberEntity member = memberService.getMemberEntityByEmail(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new AuthenticationFailedException("비밀번호가 올바르지 않습니다.");
        }

        // 회원가입 DB를 거쳐서 회원임이 검증된 이후.
        // 엑세스 토큰, 리프래쉬 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(member.getId());
        String refreshToken = jwtUtil.generateRefreshToken(member.getId());

        // 리프래쉬 토큰 만료 시점 (1주) generateRefreshToken()에서 생성한 거 말고
        // 이중으로 하나 더 넣어둔 것임.(로그아웃 로직도 관리해야돼서)
        LocalDateTime refreshExpiry = LocalDateTime.now().plusDays(7);

        // DB에 리프래쉬 토큰 저장(이미 있으면 업데이트)
        Optional<RefreshTokenEntity> optionalToken = refreshTokenRepository.findByMember(member);
        if (optionalToken.isPresent()) {
            RefreshTokenEntity tokenEntity = optionalToken.get();
            tokenEntity.updateRefreshToken(refreshToken, refreshExpiry);
            refreshTokenRepository.save(tokenEntity);
        } else {
            RefreshTokenEntity newToken = new RefreshTokenEntity(member, refreshToken,
                refreshExpiry);
            refreshTokenRepository.save(newToken);
        }

        // 응답 DTO
        // 일단 액세스 토큰, 리프래쉬 토큰, 이메일 리턴
        return new LoginDTO.LoginResponse(accessToken, refreshToken, member.getEmail());
    }

    // 리프래쉬 토큰으로 액세스 토큰 재발급
    public LoginDTO.RefreshResponseDTO refresh(LoginDTO.RefreshRequestDTO request) {
        // DB에서 리프래쉬 토큰 조회
        RefreshTokenEntity tokenEntity = refreshTokenRepository.findByRefreshToken(
                request.getRefreshToken())
            .orElseThrow(() -> new TokenInvalidException("리프래쉬 토큰이 만료되었습니다. 다시 로그인해주세요."));

        // 리프래쉬 토큰이 만료됐는지 확인
        // 리프래쉬도 만료되면 재로그인 요청해야함.
        if (tokenEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("리프래쉬 토큰이 만료되었습니다. 다시 로그인해주세요.");
        }

        // 새 액세스 토큰 발급
        Long id = tokenEntity.getMember().getId();
        String newAccessToken = jwtUtil.generateAccessToken(id);

        // 응답 DTO
        return new LoginDTO.RefreshResponseDTO(newAccessToken, tokenEntity.getRefreshToken());
    }

    // 로그아웃
    @Transactional
    public void logout(String refreshToken) {

        // 리프레쉬 토큰 엔티티 조회
        Optional<RefreshTokenEntity> optionalToken = refreshTokenRepository.findByRefreshToken(refreshToken);
        if (optionalToken.isPresent()) {
            RefreshTokenEntity tokenEntity = optionalToken.get();

            // 만료시간을 현재 시각으로 세팅하여 리프레쉬 토큰 무효화시키기 (걍 레포지토리에서 delete해도 되긴함)
            tokenEntity.setExpiryDate(LocalDateTime.now());
            refreshTokenRepository.save(tokenEntity);
        } else {
            throw new TokenInvalidException("재로그인 후 로그아웃 해주세요.");
        }
    }

}
