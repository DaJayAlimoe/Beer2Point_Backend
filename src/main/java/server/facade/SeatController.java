package server.facade;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/v1/Seat")
@Api(value = "/v1/Seat", tags = "Seats")
public class SeatController {
}
