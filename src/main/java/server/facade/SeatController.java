package server.facade;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;
import server.domain.dtos.SeatDTO;
import server.domain.entities.Seat;
import server.domain.repositories.SeatRepository;
import server.exceptions.TokenAuthenticationException;
import server.service.LogicalService;

@RestController
@RequestMapping(path = "/v1/Seat")
@Api(value = "/v1/Seat", tags = "Seats")
public class SeatController {

    private final SeatRepository seatRepository;
    private LogicalService logicalService;

    public SeatController(SeatRepository seatRepository, LogicalService logicalService) {
        this.seatRepository = seatRepository;
        this.logicalService = logicalService;
    }

    @ApiOperation(value = "Get all seats", response = Seat.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved seats"),
    })
    @GetMapping(produces={"application/json"} )
    public SeatDTO getSeats(@RequestHeader(value = "token") String token) throws TokenAuthenticationException {
        logicalService.isValidToken(token);
        return new SeatDTO(seatRepository.findAll());
    }
}
