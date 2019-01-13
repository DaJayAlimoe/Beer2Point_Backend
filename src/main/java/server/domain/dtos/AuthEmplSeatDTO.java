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
public class AuthEmplSeatDTO {

    // can be either a seat,- or employee Object
    private Seat seat;
    private Employee employee;

    public AuthEmplSeatDTO(Employee employee) {
        this.employee = employee;
    }

    public AuthEmplSeatDTO(Seat seat) {
        this.seat = seat;
    }
}
