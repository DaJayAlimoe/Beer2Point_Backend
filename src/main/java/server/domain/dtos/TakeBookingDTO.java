package server.domain.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class TakeBookingDTO {

    @ApiModelProperty(required = true)
    @NotNull
    private Long booking_id;

    @ApiModelProperty(required = true)
    @NotNull
    private Long employee_id;

}
