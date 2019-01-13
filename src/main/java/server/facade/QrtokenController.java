package server.facade;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;
import server.domain.dtos.AuthEmplSeatDTO;
import server.domain.repositories.QrtokenRepository;
import server.exceptions.TokenAuthenticationException;
import server.service.LogicalService;

@RestController
@RequestMapping(path = "/v1/User")
@Api(value = "/v1/User", tags = "Users")
public class QrtokenController {

    private final QrtokenRepository qrtokenRepository;
    private LogicalService logicalService;

    public QrtokenController(QrtokenRepository qrtokenRepository, LogicalService logicalService) {
        this.qrtokenRepository = qrtokenRepository;
        this.logicalService = logicalService;
    }

    @ApiOperation(value = "authenticate as either employee or cutomer", response = AuthEmplSeatDTO.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully authenticate"),
            @ApiResponse(code = 404, message = "Cannot authenticate person"),
    })
    @GetMapping(produces = {"application/json"})
    public AuthEmplSeatDTO authenticate(@RequestHeader(value = "token") String token) throws TokenAuthenticationException {
            return logicalService.authenticate(token);
    }
}
