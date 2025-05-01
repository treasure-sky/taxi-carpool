package edu.kangwon.university.taxicarpool.member;

import edu.kangwon.university.taxicarpool.member.dto.MemberDetailDTO;
import edu.kangwon.university.taxicarpool.member.dto.MemberPublicDTO;
import edu.kangwon.university.taxicarpool.member.dto.MemberUpdateDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    // 타인 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberPublicDTO> getPublicInfo(@PathVariable Long memberId) {
        MemberPublicDTO publicInfo = memberService.getMemberById(memberId);
        return ResponseEntity.ok(publicInfo);
    }

    // 로그인된 본인의 정보 조회
    @GetMapping("/me")
    public ResponseEntity<MemberDetailDTO> getMyInfo() {
        Long memberId = (Long) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();
        MemberDetailDTO myInfo = memberService.getDetailMember(memberId);
        return ResponseEntity.ok(myInfo);
    }

    @PatchMapping("/me")
    public ResponseEntity<MemberDetailDTO> updateMember(
        @RequestBody @Valid MemberUpdateDTO updateDTO
    ) {
        Long memberId = (Long) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();
        MemberDetailDTO updated = memberService.updateMember(memberId, updateDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<MemberDetailDTO> deleteMember(@PathVariable Long memberId) {
        MemberDetailDTO deleted = memberService.deleteMember(memberId);
        return ResponseEntity.ok(deleted);
    }
}
