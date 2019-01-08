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
import server.domain.datatypes.Item;
import server.domain.dtos.IdDTO;
import server.domain.entities.Booking;
import server.domain.repositories.EmployeeRepository;
import server.domain.repositories.BookingRepository;
import server.exceptions.EmployeeNotFoundException;
import server.exceptions.BookingAlreadyConfirmedException;
import server.exceptions.BookingAlreadyOnTheWayException;
import server.exceptions.BookingNotFoundException;
import server.service.LogicalService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(path = "/v1/order")
@Api(value = "/v1/order", tags = "Orders")
public class OrderController {

    private final LogicalService logicalService;

    private final EmployeeRepository employeeRepository;

    private final BookingRepository bookingRepository;

    private final Log log = LogFactory.getLog(getClass());

    @Autowired
    public OrderController(LogicalService logicalService,
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
    public Booking getOrder(@PathVariable("id") Long orderId) throws BookingNotFoundException {
        return bookingRepository
                .findById(orderId)
                .orElseThrow(() -> new BookingNotFoundException(orderId));
    }


    @ApiOperation(value = "Get Orders", response = Booking.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved orders"),
    })
    @GetMapping
    public List<Booking> getOrders(@RequestParam(value = "Item", required = false, defaultValue = "") String itemName) throws BookingNotFoundException {
        if (itemName.isEmpty()) {
            return bookingRepository.findAll();
        } else {
            if (!Item.isValid(itemName)) {
                throw new BookingNotFoundException(itemName);
            } else {
                return Collections.singletonList(bookingRepository
                        .findOrdersByItemName(Item.valueOf(itemName))
                        .orElseThrow(() -> new BookingNotFoundException(itemName)));
            }
        }
    }

    // If order is done -> than CLOSE
    @ApiOperation(value = "Update OrderStatus to CLOSE")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Order successfully confirmed"),
            @ApiResponse(code = 400, message = "Order was already confirmed"),
            @ApiResponse(code = 404, message = "Order is not found")
    })
    @PutMapping(value = "/{id:[\\d]+}/confirm")
    public ResponseEntity confirmOrder(@PathVariable("id") Long orderID) throws BookingNotFoundException, BookingAlreadyConfirmedException {
        logicalService.confirmOrder(orderID);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Add employee to Order")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully added employee to order"),
            @ApiResponse(code = 404, message = "employee not found")
    })
    @PostMapping(value = "/{orderid:[\\d]+}/employees/{employeeid:[\\d]+}")
    public ResponseEntity<IdDTO> addEmployee(@PathVariable("orderid") Long orderId, @PathVariable("employeeid") Long employeeId) {
        try {
            Booking booking = logicalService.addEmployeeToOrder(employeeId, orderId);
            return new ResponseEntity<>(new IdDTO(booking.getId()), HttpStatus.CREATED);
        } catch (BookingNotFoundException | EmployeeNotFoundException | BookingAlreadyOnTheWayException ex) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
