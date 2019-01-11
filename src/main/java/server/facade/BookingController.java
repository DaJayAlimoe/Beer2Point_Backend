package server.facade;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.domain.dtos.*;
import server.domain.entities.Booking;
import server.domain.repositories.EmployeeRepository;
import server.domain.repositories.BookingRepository;
import server.exceptions.*;
import server.service.LogicalService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/v1/Booking")
@Api(value = "/v1/Booking", tags = "Bookings")
public class BookingController {

    private final LogicalService logicalService;

    private final EmployeeRepository employeeRepository;

    private final BookingRepository bookingRepository;

    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    public BookingController(LogicalService logicalService,
                             EmployeeRepository employeeRepository,
                             BookingRepository bookingRepository) {
        this.logicalService = logicalService;
        this.employeeRepository = employeeRepository;
        this.bookingRepository = bookingRepository;
    }

    @ApiOperation(value = "Get an order by Id", response = Booking.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved order"),
            @ApiResponse(code = 404, message = "Order not found")
    })
    @GetMapping(value = "/{id:[\\d]+}")
    public Booking getBooking(@PathVariable("id") Long orderId) throws BookingNotFoundException {
        return bookingRepository
                .findById(orderId)
                .orElseThrow(() -> new BookingNotFoundException(orderId));
    }

    @ApiOperation(value = "Delete an order by Id", response = Booking.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted order"),
            @ApiResponse(code = 404, message = "Order not found")
    })
    @DeleteMapping
    public void deleteBooking(@RequestParam("id") Long orderId) throws BookingNotFoundException {
        Optional<Booking> booking = bookingRepository.findById(orderId);

        if(booking.isPresent())
             bookingRepository.delete(booking.get());
        else
            throw new BookingNotFoundException(orderId);
    }


    @ApiOperation(value = "Get max. 10 Orders", response = Booking.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved orders"),
    })
    @GetMapping(value = "/List")
    public List<Booking> getBookings() {
            return bookingRepository.findAll();
    }


    // If order is done -> than CLOSE
    @ApiOperation(value = "Update OrderStatus to CLOSE")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Order successfully confirmed"),
            @ApiResponse(code = 400, message = "Order was already confirmed"),
            @ApiResponse(code = 404, message = "Order is not found")
    })
    @PutMapping(value = "/{id:[\\d]+}/confirm")
    public ResponseEntity confirmBooking(@PathVariable("id") Long orderID) throws BookingNotFoundException, BookingAlreadyConfirmedException {
        logicalService.confirmBooking(orderID);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Add employee to Order")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully added employee to order"),
            @ApiResponse(code = 404, message = "employee not found")
    })
    @PostMapping
    public TokenDTO addEmployeeToBooking(@RequestParam(value = "token") String token, @Valid @RequestBody List<TakeBookingDTO> orderList) {
            String validToken =logicalService.isValidToken(token);

            for(int i = 0; i<= orderList.size(); i++){
                try {
                    logicalService.addEmployeeToBooking(orderList.get(i).getEmployee_id(),orderList.get(i).getBooking_id());
                } catch (BookingNotFoundException | BookingAlreadyOnTheWayException | EmployeeNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return new TokenDTO(validToken);
    }

    @ApiOperation(value = "Place an order")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created order"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @PostMapping(produces={"application/json"})
    @ResponseBody
    public TokenDTO createBooking(@RequestParam(value = "token") String token, @Valid @RequestBody BookingCreateDTO bookingCreateDTO) throws SeatNotFoundException, ItemNotFoundException {
            String returnToken = logicalService.isValidToken(token);
            logicalService.createBooking(bookingCreateDTO);
            return new TokenDTO(returnToken);
    }

}
