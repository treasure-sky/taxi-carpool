package edu.kangwon.university.taxicarpool.member;

import edu.kangwon.university.taxicarpool.member.dto.MemberDetailDTO;
import edu.kangwon.university.taxicarpool.member.dto.MemberPublicDTO;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public MemberDetailDTO toDetailDTO(MemberEntity entity) {
        if (entity == null) return null;
        return MemberDetailDTO.builder()
            .id(entity.getId())
            .email(entity.getEmail())
            .nickname(entity.getNickname())
            .gender(entity.getGender())
            .totalSavedAmount(entity.getTotalSavedAmount())
            .build();
    }

    public MemberPublicDTO toPublicDTO(MemberEntity entity) {
        if (entity == null) return null;
        return MemberPublicDTO.builder()
            .id(entity.getId())
            .nickname(entity.getNickname())
            .build();
    }
}
