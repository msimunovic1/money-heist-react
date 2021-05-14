package hr.msimunovic.moneyheist.api.controller;

import hr.msimunovic.moneyheist.common.Constants;
import hr.msimunovic.moneyheist.heist.Heist;
import hr.msimunovic.moneyheist.heist.HeistDTO;
import hr.msimunovic.moneyheist.heist.service.HeistService;
import hr.msimunovic.moneyheist.heist_skill.HeistSkillDTO;
import hr.msimunovic.moneyheist.util.HttpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

    @PatchMapping("/{heistId}/skills)")
    public ResponseEntity updateSkills(HttpServletRequest request,
                                       @PathVariable Long heistId,
                                       @RequestBody HeistSkillDTO heistSkillDTO) {

        // TODO: implement skills update

        String locationHeader = request.getRequestURI();

        return new ResponseEntity<>(HttpUtil.generateHttpHeaders(Constants.HTTP_HEADER_CONTENT_LOCATION, locationHeader), HttpStatus.NO_CONTENT);

    }

    /*
    SBSS-06: As a heist organiser, I want to view members eligible to participate in a heist.
    The purpose of this story is to enable the heist organiser to view members eligible to participate in a heist.

    Members returned as part of this response should conform to the following rules:
        ● Their status field should be either AVAILABLE or RETIRED .
        ● At least one of their skills should match the required skill of the heist and should have a
        level equal or higher than the required skill level.
        ● They are not confirmed members of another heist happening in the same time window
     */
}
