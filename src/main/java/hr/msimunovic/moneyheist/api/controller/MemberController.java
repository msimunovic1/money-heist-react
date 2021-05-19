package hr.msimunovic.moneyheist.api.controller;

import hr.msimunovic.moneyheist.common.Constants;
import hr.msimunovic.moneyheist.heist.dto.HeistInfoDTO;
import hr.msimunovic.moneyheist.member.Member;
import hr.msimunovic.moneyheist.member.dto.MemberDTO;
import hr.msimunovic.moneyheist.member.dto.MemberInfoDTO;
import hr.msimunovic.moneyheist.member.dto.MemberSkillDTO;
import hr.msimunovic.moneyheist.member.service.MemberService;
import hr.msimunovic.moneyheist.util.HttpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/list")
    public ResponseEntity<List<MemberInfoDTO>> getAllHeists() {
        return new ResponseEntity(memberService.getAllMembers(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity saveMember(HttpServletRequest request, @RequestBody MemberDTO memberDTO) {

        Member createdMember = memberService.saveMember(memberDTO);

        // Indicates the URL to redirect a page to.
        String locationHeader = request.getRequestURI() + "/" + createdMember.getId();

        return new ResponseEntity<>(HttpUtil.generateHttpHeaders(Constants.HTTP_HEADER_LOCATION, locationHeader), HttpStatus.CREATED);
    }

    // TODO : fix
    @PutMapping("/{memberId}/skills")
    public ResponseEntity updateSkills(HttpServletRequest request,
                                       @PathVariable Long memberId,
                                       @RequestBody MemberSkillDTO memberSkillDTO) {

        memberService.updateSkills(memberId, memberSkillDTO);

        return new ResponseEntity<>(HttpUtil.generateHttpHeaders(Constants.HTTP_HEADER_CONTENT_LOCATION, request.getRequestURI()),HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{memberId}/skills")
    public ResponseEntity deleteSkill(@PathVariable Long memberId, @RequestParam String skillName) {

        memberService.deleteSkill(memberId, skillName);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberDTO> getMemberById(@PathVariable Long memberId) {

        return new ResponseEntity<>(memberService.getMemberById(memberId), HttpStatus.OK);
    }

    @GetMapping("/{memberId}/skills")
    public ResponseEntity<MemberSkillDTO> getSkillsByMemberId(@PathVariable Long memberId) {

        return new ResponseEntity<>(memberService.getMemberSkills(memberId), HttpStatus.OK);
    }
}
