package edu.kangwon.university.taxicarpool.member;

import edu.kangwon.university.taxicarpool.auth.RefreshTokenRepository;
import edu.kangwon.university.taxicarpool.member.dto.MemberCreateDTO;
import edu.kangwon.university.taxicarpool.member.dto.MemberDetailDTO;
import edu.kangwon.university.taxicarpool.member.dto.MemberPublicDTO;
import edu.kangwon.university.taxicarpool.member.dto.MemberUpdateDTO;
import edu.kangwon.university.taxicarpool.member.exception.DuplicatedEmailException;
import edu.kangwon.university.taxicarpool.member.exception.DuplicatedNicknameException;
import edu.kangwon.university.taxicarpool.member.exception.MemberNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder,
        RefreshTokenRepository refreshTokenRepository) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public MemberDetailDTO createMember(MemberCreateDTO memberCreateDTO) {

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
        MemberDetailDTO responseDTO = new MemberDetailDTO(
            saved.getId(),
            saved.getEmail(),
            saved.getNickname(),
            saved.getGender(),
            saved.getTotalSavedAmount()
        );

        return responseDTO;
    }

    @Transactional
    public MemberDetailDTO updateMember(Long memberId, MemberUpdateDTO updateDTO) {
        if (updateDTO.isEmpty()) {
            throw new IllegalArgumentException("수정할 닉네임이나 비밀번호 중 하나 이상은 반드시 제공되어야 합니다.");
        }

        MemberEntity existedEntity = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException("회원을 찾을 수 없습니다: " + memberId));

        // 닉네임이 필드로 들어왔고, 신규 닉네임과 변경 닉네임 다를 때
        if (updateDTO.getNewNickname() != null && !updateDTO.getNewNickname()
            .equals(existedEntity.getNickname())) {

            // 이미 DB에 존재하는 닉네임이면
            if (memberRepository.existsByNickname(updateDTO.getNewNickname())) {
                throw new DuplicatedNicknameException(
                    "이미 사용 중인 닉네임입니다: " + updateDTO.getNewNickname());
            }
            existedEntity.setNickname(updateDTO.getNewNickname());
        }

        if (updateDTO.getNewPassword() != null && !updateDTO.getNewPassword().isBlank()) {
            String encodedPassword = passwordEncoder.encode(updateDTO.getNewPassword());
            existedEntity.setPassword(encodedPassword);
        }

        MemberEntity updated = memberRepository.save(existedEntity);

        return new MemberDetailDTO(
            updated.getId(),
            updated.getEmail(),
            updated.getNickname(),
            updated.getGender(),
            updated.getTotalSavedAmount()
        );
    }

    @Transactional
    public MemberDetailDTO deleteMember(Long memberId) {
        MemberEntity entity = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException("회원을 찾을 수 없습니다: " + memberId));

        refreshTokenRepository.findByMember(entity).ifPresent(refreshTokenRepository::delete);

        memberRepository.delete(entity);

        MemberDetailDTO responseDTO = new MemberDetailDTO(
            entity.getId(),
            entity.getEmail(),
            entity.getNickname(),
            entity.getGender(),
            entity.getTotalSavedAmount()
        );

        return responseDTO;
    }

    @Transactional(readOnly = true)
    public MemberPublicDTO getMemberById(Long memberId) {

        MemberEntity entity = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException("회원을 찾을 수 없습니다: " + memberId));

        MemberPublicDTO responseDTO = new MemberPublicDTO(
            entity.getId(),
            entity.getNickname()
        );

        return responseDTO;
    }

    @Transactional(readOnly = true)
    public MemberDetailDTO getDetailMember(Long memberId) {
        MemberEntity entity = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException("회원을 찾을 수 없습니다: " + memberId));

        MemberDetailDTO responseDTO = new MemberDetailDTO(
            entity.getId(),
            entity.getEmail(),
            entity.getNickname(),
            entity.getGender(),
            entity.getTotalSavedAmount()
        );

        return responseDTO;
    }

    @Transactional(readOnly = true)
    public MemberEntity getMemberEntityByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 이메일입니다: " + email));
    }

    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }


}
