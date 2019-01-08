package server.domain.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.domain.datatypes.Item;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class BookingCreateDTO {

    @ApiModelProperty(required = true)
    @Size(min = 1, max = 20)
    @NotNull
    private Item itemName;

    @ApiModelProperty(required = true)
    private int amount;

    @ApiModelProperty(required = true)
    private String seat;

}
