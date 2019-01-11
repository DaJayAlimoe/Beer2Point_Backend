package server.facade;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.domain.dtos.AuthEmplSeatDTO;
import server.domain.entities.Employee;
import server.domain.entities.Seat;
import server.domain.repositories.QrtokenRepository;
import server.exceptions.AuthentificationException;
import server.exceptions.EmployeeNotFoundException;
import server.exceptions.SeatNotFoundException;
import server.service.LogicalService;

import javax.naming.AuthenticationException;
import javax.transaction.Transactional;

@RestController
@RequestMapping(path = "/v1/Token")
@Api(value = "/v1/Token", tags = "Tokens")
public class QrtokenController {

    private final QrtokenRepository qrtokenRepository;
    private LogicalService logicalService;

    public QrtokenController(QrtokenRepository qrtokenRepository, LogicalService logicalService) {
        this.qrtokenRepository = qrtokenRepository;
        this.logicalService = logicalService;
    }

    @ApiOperation(value = "Authentificate as either employee or cutomer", response = AuthEmplSeatDTO.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully authentificate"),
    })
    @ResponseBody
    @GetMapping(produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public AuthEmplSeatDTO authentificate(@RequestBody String qrToken) throws AuthentificationException {
        try {
            AuthEmplSeatDTO authEmplSeatDTO = null;
            Object object = logicalService.authentificate(qrToken);
            if (object instanceof Employee) {
                String token = logicalService.getToken(((Employee) object).getQrtoken());
                authEmplSeatDTO = new AuthEmplSeatDTO(token, object);
            } else if (object instanceof Seat) {
                String token = logicalService.getToken(((Seat) object).getQrtoken());
                authEmplSeatDTO = new AuthEmplSeatDTO(token, object);
            }
            return authEmplSeatDTO;
        } catch (AuthentificationException ex) {
            return new AuthEmplSeatDTO("", null);
        }
    }
}
