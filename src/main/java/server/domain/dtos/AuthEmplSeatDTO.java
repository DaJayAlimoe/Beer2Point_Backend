package server.domain.dtos;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import server.domain.entities.Employee;
import server.domain.entities.Seat;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class AuthEmplSeatDTO {

    @NotNull
    private String token;

    // can be either a seat,- or employee Object
    private Object object;
}
