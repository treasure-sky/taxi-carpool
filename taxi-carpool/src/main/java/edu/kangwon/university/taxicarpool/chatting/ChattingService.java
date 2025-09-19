package edu.kangwon.university.taxicarpool.chatting;

import edu.kangwon.university.taxicarpool.chatting.dto.MessageResponseDTO;
import edu.kangwon.university.taxicarpool.chatting.dto.NotificationResponseDTO;
import edu.kangwon.university.taxicarpool.chatting.dto.ParticipantResponseDTO;
import edu.kangwon.university.taxicarpool.member.MemberEntity;
import edu.kangwon.university.taxicarpool.member.MemberRepository;
import edu.kangwon.university.taxicarpool.member.exception.MemberNotFoundException;
import edu.kangwon.university.taxicarpool.party.PartyEntity;
import edu.kangwon.university.taxicarpool.party.PartyRepository;
import edu.kangwon.university.taxicarpool.party.partyException.MemberNotInPartyException;
import edu.kangwon.university.taxicarpool.party.partyException.PartyNotFoundException;
import edu.kangwon.university.taxicarpool.party.partyException.UnauthorizedHostAccessException;
import edu.kangwon.university.taxicarpool.profanity.ProfanityService;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChattingService {

    private final MessageRepository messageRepository;
    private final PartyRepository partyRepository;
    private final MessageMapper messageMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final MemberRepository memberRepository;
    private final ProfanityService profanityService;

    ChattingService(MessageRepository messageRepository,
        PartyRepository partyRepository, MessageMapper messageMapper,
        SimpMessagingTemplate messagingTemplate, MemberRepository memberRepository,
        ProfanityService profanityService) {
        this.messageRepository = messageRepository;
        this.partyRepository = partyRepository;
        this.messageMapper = messageMapper;
        this.messagingTemplate = messagingTemplate;
        this.memberRepository = memberRepository;
        this.profanityService = profanityService;
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
        Long afterMessageId, int limit) {

        validateMemberInParty(partyId, memberId);

        Pageable pageable = PageRequest.of(0, limit);
        List<MessageEntity> messages;

        if (afterMessageId == null) {
            // afterMessageId가 null인 경우 모든 메시지를 가져옴
            messages = messageRepository.findByPartyIdOrderByIdAsc(partyId, pageable);
        } else {
            // afterMessageId가 주어진 경우 해당 ID보다 큰 메시지들을 가져옴
            messages = messageRepository.findByPartyIdAndIdGreaterThanOrderByIdAsc(partyId,
                afterMessageId, pageable);
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

    @Transactional
    public MessageResponseDTO sendMessage(Long partyId, Long memberId, String content) {
        validateMemberInParty(partyId, memberId);

        PartyEntity party = partyRepository.findById(partyId)
            .orElseThrow(() -> new PartyNotFoundException("파티를 찾을 수 없습니다."));
        MemberEntity sender = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException("멤버를 찾을 수 없습니다."));

        boolean isMember = party.getMemberEntities().stream()
            .anyMatch(m -> m.getId().equals(memberId));
        if (!isMember) {
            throw new MemberNotInPartyException("멤버가 해당 파티의 구성원이 아닙니다.");
        }

        String masked = profanityService.maskSmart(content);

        MessageEntity message = new MessageEntity(party, sender, masked, MessageType.TALK);
        messageRepository.save(message);

        return messageMapper.convertToResponseDTO(message);
    }

    @Transactional(readOnly = true)
    public List<ParticipantResponseDTO> getParticipants(Long partyId, Long memberId) {
        PartyEntity party = partyRepository.findById(partyId)
            .orElseThrow(() -> new PartyNotFoundException("파티를 찾을 수 없습니다."));

        boolean isMember = party.getMemberEntities().stream()
            .anyMatch(m -> m.getId().equals(memberId));
        if (!isMember) {
            throw new MemberNotInPartyException("멤버가 해당 파티의 구성원이 아닙니다.");
        }

        return party.getMemberEntities().stream()
            .map(m -> new ParticipantResponseDTO(m.getId(), m.getNickname()))
            .toList();
    }

    @Transactional
    public NotificationResponseDTO updateNotification(Long partyId, Long memberId,
        String notification) {

        PartyEntity party = partyRepository.findById(partyId)
            .orElseThrow(() -> new PartyNotFoundException("파티를 찾을 수 없습니다."));

        boolean isMember = party.getMemberEntities().stream()
            .anyMatch(m -> m.getId().equals(memberId));
        if (!isMember) {
            throw new MemberNotInPartyException("멤버가 해당 파티의 구성원이 아닙니다.");
        }

        // 호스트 여부 검사
        if (!party.getHostMemberId().equals(memberId)) {
            throw new UnauthorizedHostAccessException("호스트만 공지사항을 수정할 수 있습니다.");
        }

        // 공지사항 업데이트
        party.setNotification(notification);
        PartyEntity saved = partyRepository.save(party);

        return new NotificationResponseDTO(saved.getId(), saved.getNotification());
    }

}
