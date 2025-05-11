package edu.kangwon.university.taxicarpool.chatting;

import edu.kangwon.university.taxicarpool.chatting.dto.MessageCreateDTO;
import edu.kangwon.university.taxicarpool.chatting.dto.MessageResponseDTO;
import edu.kangwon.university.taxicarpool.member.MemberEntity;
import edu.kangwon.university.taxicarpool.party.PartyEntity;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    /**
     * MessageEntity를 MessageResponseDTO로 변환합니다.
     *
     * @param messageEntity 변환할 메시지 엔티티
     * @return 변환된 MessageResponseDTO 객체
     */
    public MessageResponseDTO convertToResponseDTO(MessageEntity messageEntity) {
        return new MessageResponseDTO(
            messageEntity.getId(),
            messageEntity.getSender() != null ? messageEntity.getSender().getId() : null,
            messageEntity.getContent(),
            messageEntity.getCreatedAt(),
            messageEntity.getType());
    }

    /**
     * MessageCreateDTO를 MessageEntity로 변환합니다. 일반 채팅 메시지를 생성할 때 사용됩니다.
     *
     * @param createDTO 생성할 메시지의 정보를 담은 DTO
     * @param party     메시지가 속한 파티
     * @param sender    메시지를 보낸 사용자
     * @return 생성된 MessageEntity 객체
     */
    public MessageEntity convertToEntity(MessageCreateDTO createDTO, PartyEntity party,
        MemberEntity sender) {
        return new MessageEntity(
            party,
            sender,
            createDTO.getContent(),
            MessageType.TALK // 일반 메시지는 TALK 타입
        );
    }

    /**
     * 시스템 메시지를 생성합니다. 입장(ENTER), 퇴장(LEAVE) 등의 시스템 메시지를 생성할 때 사용됩니다.
     *
     * @param party 메시지가 속한 파티
     * @param type  메시지 타입 (ENTER, LEAVE만 가능)
     * @return 생성된 MessageEntity 객체
     * @throws IllegalArgumentException type이 TALK인 경우
     */
    public MessageEntity createSystemMessage(PartyEntity party, MessageType type) {
        if (type == MessageType.TALK) {
            throw new IllegalArgumentException("시스템 메시지는 TALK 타입을 가질 수 없습니다.");
        }

        return new MessageEntity(
            party,
            null, // 시스템 메시지는 sender가 없음
            null,
            type);
    }
}