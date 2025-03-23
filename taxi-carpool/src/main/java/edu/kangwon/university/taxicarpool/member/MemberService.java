package edu.kangwon.university.taxicarpool.member;

import edu.kangwon.university.taxicarpool.member.dto.MemberCreateDTO;
import edu.kangwon.university.taxicarpool.member.dto.MemberResponseDTO;
import edu.kangwon.university.taxicarpool.member.dto.MemberUpdateDTO;
import edu.kangwon.university.taxicarpool.member.exception.DuplicatedEmailException;
import edu.kangwon.university.taxicarpool.member.exception.MemberNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponseDTO createMember(MemberCreateDTO memberCreateDTO) {

        // 이미 이메일이 존재하면 예외 처리
        if (memberRepository.existsByEmail(memberCreateDTO.getEmail())) {
            throw new DuplicatedEmailException("이미 사용 중인 이메일입니다: " + memberCreateDTO.getEmail());
        }

        // MemberEntity 생성
        MemberEntity entity = new MemberEntity();
        entity.setEmail(memberCreateDTO.getEmail());
        entity.setPassword(memberCreateDTO.getPassword());
        entity.setNickname(memberCreateDTO.getNickname());
        entity.setGender(memberCreateDTO.getGender());

        // DB 저장
        MemberEntity saved = memberRepository.save(entity);

        // 저장된 결과를 DTO로 변환하여 반환
        MemberResponseDTO responseDTO = new MemberResponseDTO();
        responseDTO.setId(saved.getId());
        responseDTO.setEmail(saved.getEmail());
        responseDTO.setNickname(saved.getNickname());
        responseDTO.setGender(saved.getGender());

        return responseDTO;
    }

    public MemberResponseDTO updateMember(Long memberId, MemberUpdateDTO updateDTO) {
        MemberEntity existing = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException("회원을 찾을 수 없습니다: " + memberId));

        existing.setPassword(updateDTO.getNewPassword());
        existing.setNickname(updateDTO.getNewNickname());

        MemberEntity updated = memberRepository.save(existing);

        MemberResponseDTO responseDTO = new MemberResponseDTO();
        responseDTO.setId(updated.getId());
        responseDTO.setEmail(updated.getEmail());
        responseDTO.setNickname(updated.getNickname());
        responseDTO.setGender(updated.getGender());

        return responseDTO;
    }

    public MemberResponseDTO deleteMember(Long memberId) {
        return null;
    }

    public MemberResponseDTO getMember(Long memberId) {

        MemberEntity entity = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException("회원을 찾을 수 없습니다: " + memberId));

        MemberResponseDTO responseDTO = new MemberResponseDTO();
        responseDTO.setId(entity.getId());
        responseDTO.setEmail(entity.getEmail());
        responseDTO.setNickname(entity.getNickname());
        responseDTO.setGender(entity.getGender());

        return responseDTO;
    }
}
