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

    @Autowired
    public AuthService(MemberRepository memberRepository,
        PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

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

        // DB 저장(일단 레포지토리에 직접 저장해놓았는데, 추후에 merger되면 memberService로 바꿔서 해도 괜찮을듯..?)
        MemberEntity savedMember = memberRepository.save(member);

        // 응답 DTO 만들기
        return new SignUpDTO.SignUpResponseDTO(
            savedMember.getId(),
            savedMember.getEmail(),
            savedMember.getNickname(),
            savedMember.getGender()
        );
    }

}
