package hr.msimunovic.moneyheist.heist.dto;

import hr.msimunovic.moneyheist.common.enums.HeistStatusEnum;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transformation Object for work with API-s that request or response general Heist data.
 */
@Data
@Builder
public class HeistDTO {

    @NotEmpty(message = "Heist name is required")
    private String name;

    @NotEmpty(message = "Location is required")
    private String location;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Valid
    @NotEmpty(message = "Skill is required")
    private List<HeistSkillDTO> skills;

    private HeistStatusEnum status;

}
