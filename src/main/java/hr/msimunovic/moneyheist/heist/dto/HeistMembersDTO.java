package hr.msimunovic.moneyheist.heist.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class HeistMembersDTO {

    @NotEmpty(message = "At least one member is required.")
    private String[] members;
}
