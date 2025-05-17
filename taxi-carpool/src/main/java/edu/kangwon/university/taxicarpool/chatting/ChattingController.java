package edu.kangwon.university.taxicarpool.chatting;

import edu.kangwon.university.taxicarpool.chatting.dto.MessageResponseDTO;
import edu.kangwon.university.taxicarpool.chatting.dto.ParticipantResponseDTO;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/party/{partyId}")
public class ChattingController {

    private final ChattingService chattingService;

    public ChattingController(ChattingService chattingService) {
        this.chattingService = chattingService;
    }

    @GetMapping("/messages")
    public ResponseEntity<List<MessageResponseDTO>> getMessageHistory(@PathVariable Long partyId,
        @RequestParam(required = false) Long afterMessageId) {
        // JWT 토큰에서 사용자 ID 추출
        Long memberId = (Long) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();

        List<MessageResponseDTO> messages = chattingService.getMessageHistory(partyId, memberId,
            afterMessageId);

        return ResponseEntity.ok(messages);
    }

    @GetMapping("/participants")
    public ResponseEntity<List<ParticipantResponseDTO>> getParticipants(
        @PathVariable Long partyId) {
        Long memberId = (Long) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();
        List<ParticipantResponseDTO> list = chattingService.getParticipants(partyId, memberId);
        return ResponseEntity.ok(list);
    }

}
