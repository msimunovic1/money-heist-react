package hr.msimunovic.moneyheist.api.controller;

import hr.msimunovic.moneyheist.common.Constants;
import hr.msimunovic.moneyheist.heist.Heist;
import hr.msimunovic.moneyheist.heist.dto.*;
import hr.msimunovic.moneyheist.heist.service.HeistService;
import hr.msimunovic.moneyheist.heist_member.dto.MembersEligibleForHeistDTO;
import hr.msimunovic.moneyheist.util.HttpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/heist")
public class HeistController {

    private final HeistService heistService;

    @GetMapping("/list")
    public ResponseEntity<List<HeistInfoDTO>> getAllHeists() {
        return new ResponseEntity(heistService.getAllHeists(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity saveHeist(HttpServletRequest request, @Valid @RequestBody HeistDTO heistDTO) {

        Heist createdHeist = heistService.saveHeist(heistDTO);

        heistService.scheduleStartEndHeist(createdHeist);

        // Indicates the URL to redirect a page to.
        String locationHeader = request.getRequestURI() + "/" + createdHeist.getId();

        return new ResponseEntity<>(HttpUtil.generateHttpHeaders(Constants.HTTP_HEADER_LOCATION, locationHeader), HttpStatus.CREATED);

    }

    @PatchMapping(value = "/{heistId}/skills")
    public ResponseEntity updateSkills(HttpServletRequest request,
                                       @PathVariable Long heistId,
                                       @Valid @RequestBody HeistSkillsDTO heistSkillsDTO) {

        heistService.updateSkills(heistId, heistSkillsDTO);

        return new ResponseEntity<>(HttpUtil.generateHttpHeaders(Constants.HTTP_HEADER_CONTENT_LOCATION, request.getRequestURI()), HttpStatus.NO_CONTENT);

    }

    @GetMapping("/{heistId}/eligible_members")
    public ResponseEntity<MembersEligibleForHeistDTO> getMembersEligibleForHeist(@PathVariable Long heistId) {

        return new ResponseEntity<>(heistService.getMembersEligibleForHeist(heistId), HttpStatus.OK);
    }

    @PutMapping("/{heistId}/members")
    public ResponseEntity saveHeistMembers(HttpServletRequest request,
                                           @PathVariable Long heistId,
                                           @Valid @RequestBody HeistMembersDTO heistMembersDTO) {

        heistService.saveHeistMembers(heistId, heistMembersDTO);

        return new ResponseEntity<>(HttpUtil.generateHttpHeaders(Constants.HTTP_HEADER_CONTENT_LOCATION, request.getRequestURI()), HttpStatus.NO_CONTENT);
    }

    @PutMapping("{heistId}/start")
    public ResponseEntity startHeistManually(HttpServletRequest request, @PathVariable Long heistId) {

        heistService.startHeist(heistId);

        return new ResponseEntity<>(HttpUtil.generateHttpHeaders(Constants.HTTP_HEADER_LOCATION, request.getRequestURI()), HttpStatus.OK);
    }

    @GetMapping("/{heistId}")
    public ResponseEntity<HeistDTO> getHeistById(@PathVariable Long heistId) {

        return new ResponseEntity<>(heistService.getHeistById(heistId), HttpStatus.OK);
    }

    @GetMapping("/{heistId}/skills")
    public ResponseEntity<List<HeistSkillDTO>> getSkillsByHeistId(@PathVariable Long heistId) {

        return new ResponseEntity<>(heistService.getHeistSkills(heistId), HttpStatus.OK);
    }

    @GetMapping("{heistId}/members")
    public ResponseEntity<List<HeistMemberDTO>> getHeistMembers(@PathVariable Long heistId) {

        return new ResponseEntity<>(heistService.getHeistMembers(heistId), HttpStatus.OK);
    }

    @GetMapping("{heistId}/status")
    public ResponseEntity<HeistStatusDTO> getHeistStatus(@PathVariable Long heistId) {

        return new ResponseEntity<>(heistService.getHeistStatus(heistId), HttpStatus.OK);
    }

    @GetMapping("/{heistId}/outcome")
    public ResponseEntity<HeistOutcomeDTO> getHeistOutcome(@PathVariable Long heistId) {

        return new ResponseEntity<>(heistService.getHeistOutcome(heistId), HttpStatus.OK);
    }

}
