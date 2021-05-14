package hr.msimunovic.moneyheist.api.controller;

import hr.msimunovic.moneyheist.common.Constants;
import hr.msimunovic.moneyheist.member.Member;
import hr.msimunovic.moneyheist.member.MemberDTO;
import hr.msimunovic.moneyheist.member.service.MemberService;
import hr.msimunovic.moneyheist.util.HttpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping()
    public ResponseEntity saveMember(HttpServletRequest request, @RequestBody MemberDTO member) {

        Member createdMember = memberService.saveMember(member);

        // Indicates the URL to redirect a page to.
        String locationHeader = request.getRequestURI() + "/" + createdMember.getId();

        return new ResponseEntity<>(HttpUtil.generateHttpHeaders(Constants.HTTP_HEADER_LOCATION, locationHeader), HttpStatus.CREATED);
    }
}
