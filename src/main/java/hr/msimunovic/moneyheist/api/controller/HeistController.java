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
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/heist")
@RestController
public class HeistController {

    private final HeistService heistService;

    /**
     * Add a new heist.
     * @param request       Request URI
     * @param heistDTO      Request Data
     * @return              201 and Location Header if heist is saved to DB, 400 if exception is thrown
     */
    @PostMapping("")
    public ResponseEntity saveHeist(HttpServletRequest request, @RequestBody HeistDTO heistDTO) {

        Heist createdHeist = heistService.saveHeist(heistDTO);

        // Indicates the URL to redirect a page to.
        String locationHeader = request.getRequestURI() + "/" + createdHeist.getId();

        return new ResponseEntity<>(HttpUtil.generateHttpHeaders(Constants.HTTP_HEADER_LOCATION, locationHeader), HttpStatus.CREATED);

    }

    // TODO: implement
    @PatchMapping("/{heistId}/skills)")
    public ResponseEntity updateSkills(HttpServletRequest request,
                                       @PathVariable Long heistId,
                                       @RequestBody HeistSkillDTO heistSkillDTO) {


        return new ResponseEntity<>(HttpUtil.generateHttpHeaders(Constants.HTTP_HEADER_CONTENT_LOCATION, request.getRequestURI()), HttpStatus.NO_CONTENT);

    }

    // TODO: implement
    @GetMapping("/{heistId}/eligible_members")
    public ResponseEntity<MembersEligibleForHeistDTO> getMembersEligibleForHeist(@PathVariable Long heistId) {

        MembersEligibleForHeistDTO membersEligibleForHeist = heistService.getMembersEligibleForHeist(heistId);

        return new ResponseEntity<>(membersEligibleForHeist, HttpStatus.OK);
    }

    //TODO: implement
    @PutMapping("/{heistId}/members")
    public ResponseEntity saveHeistMembers(HttpServletRequest request, @RequestBody HeistMembersDTO heistMembersDTO) {

        return new ResponseEntity<>(HttpUtil.generateHttpHeaders(Constants.HTTP_HEADER_CONTENT_LOCATION, request.getRequestURI()), HttpStatus.NO_CONTENT);
    }

    // TODO: implement
    @PutMapping("{heistId}/start")
    public ResponseEntity startHeistManually(HttpServletRequest request, @PathVariable Long heistId) {

        return new ResponseEntity<>(HttpUtil.generateHttpHeaders(Constants.HTTP_HEADER_LOCATION, request.getRequestURI()), HttpStatus.OK);
    }

    @GetMapping("/{heistId}")
    public ResponseEntity<HeistDTO> getHeistById(@PathVariable Long heistId) {

        return new ResponseEntity<>(heistService.getHeistById(heistId), HttpStatus.OK);
    }

    @GetMapping("/{heistId}/skills")
    public ResponseEntity<List<HeistSkillDTO>> getSkillsByHeistId(@PathVariable Long heistId) {

        return new ResponseEntity<>(heistService.getSkillsByHeistId(heistId), HttpStatus.OK);
    }

    // TODO: implement
    @GetMapping("{heistId}/members")
    public ResponseEntity<?> getHeistMembers(@PathVariable Long heistId) {

        return new ResponseEntity<>(HttpStatus.OK);
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
