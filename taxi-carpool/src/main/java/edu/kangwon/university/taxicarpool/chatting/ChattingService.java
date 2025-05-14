package edu.kangwon.university.taxicarpool.chatting;

import edu.kangwon.university.taxicarpool.chatting.dto.MessageResponseDTO;
import edu.kangwon.university.taxicarpool.member.MemberEntity;
import edu.kangwon.university.taxicarpool.party.PartyEntity;
import edu.kangwon.university.taxicarpool.party.PartyRepository;
import edu.kangwon.university.taxicarpool.party.partyException.MemberNotInPartyException;
import edu.kangwon.university.taxicarpool.party.partyException.PartyNotFoundException;
import java.util.List;
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

    /**
     * 메시지 히스토리 조회
     */
    @Transactional(readOnly = true)
    public List<MessageResponseDTO> getMessageHistory(Long partyId, Long memberId,
        Long afterMessageId) {

        validateMemberInParty(partyId, memberId);

        List<MessageEntity> messages;

        if (afterMessageId == null) {
            // afterMessageId가 null인 경우 모든 메시지를 가져옴
            messages = messageRepository.findByPartyIdOrderByIdAsc(partyId);
        } else {
            // afterMessageId가 주어진 경우 해당 ID보다 큰 메시지들을 가져옴
            messages = messageRepository.findByPartyIdAndIdGreaterThanOrderByIdAsc(partyId,
                afterMessageId);
        }

        return messages.stream()
            .map(messageMapper::convertToResponseDTO).toList();

    }

    /**
     * 사용자가 파티에 속해 있는지 검증
     *
     * @throws MemberNotInPartyException 사용자가 파티에 속해있지 않은 경우
     */
    private void validateMemberInParty(Long partyId, Long memberId) {
        PartyEntity party = partyRepository.findById(partyId)
            .orElseThrow(() -> new PartyNotFoundException("파티를 찾을 수 없습니다."));

        boolean isMember = party.getMemberEntities().stream()
            .anyMatch(member -> member.getId().equals(memberId));

        if (!isMember) {
            throw new MemberNotInPartyException("해당 파티의 멤버가 아닙니다.");
        }
    }

}
