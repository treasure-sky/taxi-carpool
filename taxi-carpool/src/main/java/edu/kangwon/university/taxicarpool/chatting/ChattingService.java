package edu.kangwon.university.taxicarpool.chatting;

import edu.kangwon.university.taxicarpool.member.MemberEntity;
import edu.kangwon.university.taxicarpool.party.PartyEntity;
import edu.kangwon.university.taxicarpool.party.PartyRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChattingService {

    private final MessageRepository messageRepository;
    private final PartyRepository partyRepository;
    private final MessageMapper messageMapper;
    private final SimpMessagingTemplate messagingTemplate;

    ChattingService(MessageRepository messageRepository,
        PartyRepository partyRepository, MessageMapper messageMapper,
        SimpMessagingTemplate messagingTemplate) {
        this.messageRepository = messageRepository;
        this.partyRepository = partyRepository;
        this.messageMapper = messageMapper;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * 파티 입장/퇴장 시 시스템 메시지 생성 및 전송
     */
    @Transactional
    public void createSystemMessage(PartyEntity partyEntity, MemberEntity memberEntity,
        MessageType type) {
        // PartyService에서 Party, Member 존재여부 및 참여여부 검증이 이루어지므로 여기서는 검증하지 않음
        if (type == MessageType.TALK) {
            throw new IllegalArgumentException("시스템 메시지는 TALK 타입을 가질 수 없습니다.");
        }

        String displayType = type.getDisplayName();
        String content = memberEntity.getNickname() + "님이 " + displayType + "하셨습니다.";

        MessageEntity message = new MessageEntity(partyEntity, memberEntity, content,
            type);
        messageRepository.save(message);

        // WebSocket을 통해 메시지 브로드캐스트
        messagingTemplate.convertAndSend("/sub/party/" + partyEntity.getId(),
            messageMapper.convertToResponseDTO(message));
    }

}
