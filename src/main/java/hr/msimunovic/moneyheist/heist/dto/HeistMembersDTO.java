package hr.msimunovic.moneyheist.heist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeistMembersDTO {

    @NotEmpty(message = "At least one member is required.")
    private String[] members;
}
