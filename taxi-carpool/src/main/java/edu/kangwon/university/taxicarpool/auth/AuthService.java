package edu.kangwon.university.taxicarpool.auth;

import edu.kangwon.university.taxicarpool.auth.authException.AuthenticationFailedException;
import edu.kangwon.university.taxicarpool.auth.authException.TokenInvalidException;
import edu.kangwon.university.taxicarpool.member.MemberEntity;
import edu.kangwon.university.taxicarpool.member.MemberRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public AuthService(MemberRepository memberRepository,
        PasswordEncoder passwordEncoder,
        JwtUtil jwtUtil,
        RefreshTokenRepository refreshTokenRepository) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // 회원가입
    public SignUpDTO.SignUpResponseDTO signUp(SignUpDTO.SignUpRequestDTO request) {
        // 이메일 중복 체크
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AuthenticationFailedException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 암호화 -> 엔티티 만들기(encode()가 PasswordEncoder에서 제공하는 암호화 메서드임.(구현체를 override말고, config에 구현해서 사용해야함))
        MemberEntity member = new MemberEntity(
            request.getEmail(),
            passwordEncoder.encode(request.getPassword()),
            request.getNickname(),
            request.getGender()
        );

        // DB 저장(일단 레포지토리에 직접 저장해놓았는데, 추후에 merge되면 memberService로 바꿔서 해도 괜찮을듯..?)
        MemberEntity savedMember = memberRepository.save(member);

        // 응답 DTO 만들기
        return new SignUpDTO.SignUpResponseDTO(
            savedMember.getId(),
            savedMember.getEmail(),
            savedMember.getNickname(),
            savedMember.getGender()
        );
    }

    // 로그인
    public LoginDTO.LoginResponse login(LoginDTO.LoginRequest request) {
        MemberEntity member = memberRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new AuthenticationFailedException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new AuthenticationFailedException("비밀번호가 올바르지 않습니다.");
        }

        // 회원가입 DB를 거쳐서 회원임이 검증된 이후.
        // 엑세스 토큰, 리프래쉬 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(member.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(member.getEmail());

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
            RefreshTokenEntity newToken = new RefreshTokenEntity(member, refreshToken, refreshExpiry);
            refreshTokenRepository.save(newToken);
        }

        // 응답 DTO
        // 일단 액세스 토큰, 리프래쉬 토큰, 이메일 리턴
        return new LoginDTO.LoginResponse(accessToken, refreshToken, member.getEmail());
    }

    // 리프래쉬 토큰으로 액세스 토큰 재발급
    public LoginDTO.RefreshResponseDTO refresh(LoginDTO.RefreshRequestDTO request) {
        // DB에서 리프래쉬 토큰 조회
        RefreshTokenEntity tokenEntity = refreshTokenRepository.findByRefreshToken(request.getRefreshToken())
            .orElseThrow(() -> new TokenInvalidException("유효하지 않은 리프래쉬 토큰입니다."));

        // 리프래쉬 토큰이 만료됐는지 확인
        // 리프래쉬도 만료되면 재로그인 요청해야함.
        if (tokenEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new TokenInvalidException("리프래쉬 토큰이 만료되었습니다. 다시 로그인해주세요.");
        }

        // 새 액세스 토큰 발급
        String email = tokenEntity.getMember().getEmail();
        String newAccessToken = jwtUtil.generateAccessToken(email);

        // 응답 DTO
        return new LoginDTO.RefreshResponseDTO(newAccessToken, tokenEntity.getRefreshToken());
    }

    public void logout(String email) {
        // 1) 이메일로 Member 조회
        MemberEntity member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new AuthenticationFailedException("존재하지 않는 이메일입니다."));

        // 2) 리프레쉬 토큰 엔티티 조회
        Optional<RefreshTokenEntity> optionalToken = refreshTokenRepository.findByMember(member);
        if (optionalToken.isPresent()) {
            RefreshTokenEntity tokenEntity = optionalToken.get();

            // 만료시간을 현재 시각으로 세팅하여 무효화시키기 (걍 레포지토리에서 delete해도 되긴함)
            tokenEntity.setExpiryDate(LocalDateTime.now());
            refreshTokenRepository.save(tokenEntity);
        }
        else {
            throw new TokenInvalidException("토큰없는 로그아웃입니다.");
        }
    }

}
