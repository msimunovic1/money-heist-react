package hr.msimunovic.moneyheist.heist.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transformation Object for work with API-s that request or response general Heist data.
 */
@Data
public class HeistDTO {

    private String name;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<HeistSkillDTO> skills;

}
