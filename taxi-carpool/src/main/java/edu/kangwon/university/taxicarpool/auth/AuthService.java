package edu.kangwon.university.taxicarpool.auth;

import edu.kangwon.university.taxicarpool.member.MemberEntity;
import edu.kangwon.university.taxicarpool.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(MemberRepository memberRepository,
        PasswordEncoder passwordEncoder,
        JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // 회원가입
    public SignUpDTO.SignUpResponseDTO signUp(SignUpDTO.SignUpRequestDTO request) {
        // 이메일 중복 체크
        // 예외처리 대충 해놓음.
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
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
        // 예외처리 일단 대충만 해놓음
        MemberEntity member = memberRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }

        // 회원가입 DB를 거쳐서 회원임이 검증된 이후.
        // JWT 토큰 생성
        String token = jwtUtil.generateToken(member.getEmail());

        // 응답 DTO
        // 일단 토큰이랑 이메일까지 리턴해주는 것으로 구현함.
        return new LoginDTO.LoginResponse(token, member.getEmail());
    }

}
