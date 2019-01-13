package server.domain.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.domain.entities.Qrtoken;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class EmployeeCreateDTO {

    @ApiModelProperty(required = true)
    @Size(min = 1, max = 20)
    @NotNull
    private String lastName;

    @ApiModelProperty(required = true)
    @NotNull
    private Qrtoken Qrtoken;

}
