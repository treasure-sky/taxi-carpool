package edu.kangwon.university.taxicarpool.member;

import edu.kangwon.university.taxicarpool.member.dto.MemberResponseDTO;
import edu.kangwon.university.taxicarpool.member.dto.MemberUpdateDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDTO> getMember(@PathVariable Long memberId) {
        MemberResponseDTO member = memberService.getMemberById(memberId);
        return ResponseEntity.ok(member);
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<MemberResponseDTO> updateMember(
        @PathVariable Long memberId,
        @RequestBody @Valid MemberUpdateDTO updateDTO
    ) {
        MemberResponseDTO updated = memberService.updateMember(memberId, updateDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<MemberResponseDTO> deleteMember(@PathVariable Long memberId) {
        MemberResponseDTO deleted = memberService.deleteMember(memberId);
        return ResponseEntity.ok(deleted);
    }
}
