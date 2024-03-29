package server.domain.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.domain.entities.Item;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class BookingCreateDTO {

    @ApiModelProperty(required = true)
    @NotNull
    private Long item_id;

    @ApiModelProperty(required = true)
    private int amount;

    @ApiModelProperty(required = true)
    @NotNull
    private Long seat_id;

}
