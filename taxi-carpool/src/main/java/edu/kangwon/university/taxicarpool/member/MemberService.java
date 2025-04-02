package edu.kangwon.university.taxicarpool.member;

import edu.kangwon.university.taxicarpool.member.dto.MemberCreateDTO;
import edu.kangwon.university.taxicarpool.member.dto.MemberResponseDTO;
import edu.kangwon.university.taxicarpool.member.dto.MemberUpdateDTO;
import edu.kangwon.university.taxicarpool.member.exception.DuplicatedEmailException;
import edu.kangwon.university.taxicarpool.member.exception.DuplicatedNicknameException;
import edu.kangwon.university.taxicarpool.member.exception.MemberNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public MemberResponseDTO createMember(MemberCreateDTO memberCreateDTO) {

        // 이미 이메일이 존재하면 예외 처리
        if (memberRepository.existsByEmail(memberCreateDTO.getEmail())) {
            throw new DuplicatedEmailException("이미 사용 중인 이메일입니다: " + memberCreateDTO.getEmail());
        }

        // 이미 닉네임이 존재하면 예외 처리
        if (memberRepository.existsByNickname(memberCreateDTO.getNickname())) {
            throw new DuplicatedNicknameException(
                "이미 사용 중인 닉네임입니다: " + memberCreateDTO.getNickname());
        }

        // MemberEntity 생성
        MemberEntity entity = new MemberEntity();
        entity.setEmail(memberCreateDTO.getEmail());
        entity.setNickname(memberCreateDTO.getNickname());
        entity.setGender(memberCreateDTO.getGender());

        // password 암호화
        String encodedPassword = passwordEncoder.encode(memberCreateDTO.getPassword());
        entity.setPassword(encodedPassword);

        // DB 저장
        MemberEntity saved = memberRepository.save(entity);

        // 저장된 결과를 DTO로 변환하여 반환
        MemberResponseDTO responseDTO = new MemberResponseDTO(
            saved.getId(),
            saved.getEmail(),
            saved.getNickname(),
            saved.getGender()
        );

        return responseDTO;
    }


    public MemberResponseDTO updateMember(Long memberId, MemberUpdateDTO updateDTO) {
        MemberEntity existedEntity = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException("회원을 찾을 수 없습니다: " + memberId));

        // 기존 닉네임으로 변경시에는 예외처리하지 않음
        if (!existedEntity.getNickname().equals(updateDTO.getNewNickname())
            && memberRepository.existsByNickname(updateDTO.getNewNickname())) {
            throw new DuplicatedNicknameException("이미 사용 중인 닉네임입니다: " + updateDTO.getNewNickname());
        }
        existedEntity.setNickname(updateDTO.getNewNickname());

        String encodedPassword = passwordEncoder.encode(updateDTO.getNewPassword());
        existedEntity.setPassword(encodedPassword);

        MemberEntity updated = memberRepository.save(existedEntity);

        MemberResponseDTO responseDTO = new MemberResponseDTO(
            updated.getId(),
            updated.getEmail(),
            updated.getNickname(),
            updated.getGender()
        );

        return responseDTO;
    }

    public MemberResponseDTO deleteMember(Long memberId) {
        MemberEntity entity = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException("회원을 찾을 수 없습니다: " + memberId));

        memberRepository.delete(entity);

        MemberResponseDTO responseDTO = new MemberResponseDTO(
            entity.getId(),
            entity.getEmail(),
            entity.getNickname(),
            entity.getGender()
        );

        return responseDTO;
    }

    public MemberResponseDTO getMember(Long memberId) {

        MemberEntity entity = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException("회원을 찾을 수 없습니다: " + memberId));

        MemberResponseDTO responseDTO = new MemberResponseDTO(
            entity.getId(),
            entity.getEmail(),
            entity.getNickname(),
            entity.getGender()
        );

        return responseDTO;
    }
}
