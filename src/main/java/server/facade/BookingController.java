package server.facade;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import server.domain.datatypes.BookingStatus;
import server.domain.dtos.*;
import server.domain.entities.Booking;
import server.domain.entities.Employee;
import server.domain.entities.Qrtoken;
import server.domain.entities.Seat;
import server.domain.repositories.BookingRepository;
import server.domain.repositories.EmployeeRepository;
import server.domain.repositories.QrtokenRepository;
import server.domain.repositories.SeatRepository;
import server.exceptions.*;
import server.service.LogicalService;

import javax.validation.Valid;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/v1/Booking")
@Api(value = "/v1/Booking", tags = "Bookings")
public class BookingController {

    private final LogicalService logicalService;

    private final BookingRepository bookingRepository;

    private final SeatRepository seatRepository;

    private final EmployeeRepository employeeRepository;

    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    public BookingController(LogicalService logicalService,
                             BookingRepository bookingRepository, SeatRepository seatRepository, EmployeeRepository employeeRepository) {
        this.logicalService = logicalService;
        this.bookingRepository = bookingRepository;
        this.seatRepository = seatRepository;
        this.employeeRepository = employeeRepository;
    }


    @ApiOperation(value = "Get an order by Id, just for testing", response = Booking.class)
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


    @ApiOperation(value = "Delete an order by Id if not on the Way or closed", response = Booking.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted order"),
            @ApiResponse(code = 404, message = "Order not found")
    })
    @PutMapping
    public void deleteBooking(@RequestHeader(value = "token") String token, @RequestBody String json) throws BookingNotFoundException, SeatTokenWrongException {
        logicalService.isValidSeat(token);
        JSONObject jsonBody = new JSONObject(json);
        Long orderId = jsonBody.getLong("id");
        Optional<Booking> bookingOption = bookingRepository.findById(orderId);

        if (bookingOption.isPresent()) {
            Booking booking = bookingOption.get();
            booking.updateOrderStatus(BookingStatus.CANCELED);
            bookingRepository.save(booking);
        } else
            throw new BookingNotFoundException(orderId);
    }


    @ApiOperation(value = "Place an order")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created order"),
            @ApiResponse(code = 400, message = "Cant create order"),
            @ApiResponse(code = 401, message = "Unauthorized client")
    })
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void createBooking(@RequestHeader(value = "token") String seatToken, @Valid @RequestBody List<BookingCreateDTO> bookingCreateDTOList) throws ItemNotFoundException, SeatTokenWrongException {

        logicalService.isValidSeat(seatToken);
        Long seatID = bookingCreateDTOList.get(0).getSeat_id();
        Optional<Seat> seat = seatRepository.findById(seatID);
        for (BookingCreateDTO bc : bookingCreateDTOList) {
            logicalService.createBooking(bc.getItem_id(), seat.get(), bc.getAmount());
        }
    }


    @ApiOperation(value = "Add employee to Order")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully added employee to order"),
            @ApiResponse(code = 404, message = "employee not found"),
            @ApiResponse(code = 401, message = "Unauthorized client")
    })
    @PutMapping
    @ResponseStatus(value = HttpStatus.OK)
    public BookingDTO addEmployeeToBooking(@RequestHeader(value = "token") String emplToken, @Valid @RequestBody List<TakeBookingDTO> orderList) throws EmployeeTokenWrongException, BookingNotFoundException, BookingAlreadyOnTheWayException {

        logicalService.isValidEmployee(emplToken);
        Optional<Employee> optionalEmployee = employeeRepository.findByQrtokenToken(emplToken);

        for (TakeBookingDTO tb : orderList) {
            logicalService.addEmployeeToBooking(optionalEmployee.get(), tb.getBooking_id());
        }
        return new BookingDTO(bookingRepository.findFirst20ByActiveAtIsLessThanEqualAndStatusIsOrderByCreatedOn(LocalDateTime.now(), BookingStatus.PREORDERED));
    }


    @ApiOperation(value = "Get max. 20 Orders", response = Booking.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved orders"),
    })
    @GetMapping(value = "/List", produces = {"application/json"})
    public BookingDTO getBookings(@RequestHeader(value = "token") String token) throws EmployeeTokenWrongException {
        logicalService.isValidEmployee(token);
        return new BookingDTO(bookingRepository.findFirst20ByActiveAtIsLessThanEqualAndStatusIsOrderByCreatedOn(LocalDateTime.now(), BookingStatus.PREORDERED));
    }


    @ApiOperation(value = "Get my orders", response = Booking.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved orders"),
    })
    @GetMapping(produces = {"application/json"})
    public BookingDTO getMyBookings(@RequestHeader(value = "token") String token) throws TokenNotFoundException {

        if(logicalService.isEmployeeToken(token)){
            return new BookingDTO(bookingRepository.findFirst30ByEmployee_QrtokenTokenOrderByCreatedOn(token));
        }
        else if(logicalService.isSeatToken(token)){
            return new BookingDTO(bookingRepository.findFirst30BySeat_QrtokenTokenOrderByCreatedOn(token));
        }else{
            throw new TokenNotFoundException(token);
        }
    }


    // If order is done -> than CLOSE
    @ApiOperation(value = "Update OrderStatus to CLOSE")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Order successfully confirmed"),
            @ApiResponse(code = 400, message = "Order was already confirmed"),
            @ApiResponse(code = 404, message = "Order is not found")
    })
    @PutMapping(value = "/{id:[\\d]+}")
    public void confirmBooking(@RequestBody String json, @RequestHeader(value = "token") String token) throws BookingNotFoundException, BookingAlreadyConfirmedException, EmployeeTokenWrongException {
        logicalService.isValidEmployee(token);
        JSONObject jsonBody = new JSONObject(json);
        Long orderId = jsonBody.getLong("id");
        logicalService.confirmBooking(orderId, token);
    }
}
