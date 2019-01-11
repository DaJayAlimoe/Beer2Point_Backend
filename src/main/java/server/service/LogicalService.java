package server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.domain.datatypes.OrderStatus;
import server.domain.dtos.BookingCreateDTO;
import server.domain.dtos.EmployeeCreateDTO;
import server.domain.dtos.IdDTO;
import server.domain.entities.*;
import server.domain.repositories.*;
import server.exceptions.*;

import java.util.List;
import java.util.Optional;

@Service
public class LogicalService {

    private final EmployeeRepository employeeRepository;

    private final BookingRepository bookingRepository;

    private final ItemRepository itemRepository;

    private final SeatRepository seatRepository;

    private final QrtokenRepository qrtokenRepository;

    @Autowired // cyclic dependency resolution; see http://www.baeldung.com/circular-dependencies-in-spring
    public LogicalService(EmployeeRepository employeeRepository, BookingRepository bookingRepository, ItemRepository itemRepository, SeatRepository seatRepository, QrtokenRepository qrtokenRepository) {
        this.employeeRepository = employeeRepository;
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.seatRepository = seatRepository;
        this.qrtokenRepository = qrtokenRepository;
    }

    //++++++++++++++++++++++++++for Employees++++++++++++++++++++++++++++++++++++++++++++++++++++++

    // @Transactional means that other transaction have to w8 until succesfull. Also u can
    // add a rollback for thrown exceptions if anything wents wrong.
    // get all employees as a List
    @Transactional(readOnly = true)
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }


    // delete employee by id. implicit call oneToMany Bean
    @Transactional(rollbackFor = {EmployeeNotFoundException.class})
    public Employee deleteEmployee(Long employeeId) throws EmployeeNotFoundException {
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        Employee employee;

        if (employeeOptional.isPresent()) {
            employee = employeeOptional.get();
            employeeRepository.delete(employeeOptional.get());
        } else {
            throw new EmployeeNotFoundException(employeeId);
        }

        return employee;
    }

    // create employee
    @Transactional(rollbackFor = {ResourceNotFoundException.class})
    public IdDTO createEmployee(EmployeeCreateDTO employeeCreateDTO) {
        Employee employee = employeeRepository.save(Employee.of(employeeCreateDTO));

        return new IdDTO(employee.getId());
    }


    //++++++++++++++++++++++++++for Orders++++++++++++++++++++++++++++++++++++++++++++++++++++++


    // update Order status
    @Transactional(rollbackFor = {BookingNotFoundException.class, BookingAlreadyConfirmedException.class})
    public void confirmBooking(Long orderID) throws BookingAlreadyConfirmedException, BookingNotFoundException {
        Booking booking = bookingRepository
                .findById(orderID)
                .orElseThrow(() -> new BookingNotFoundException(orderID));

        if (booking.getOrderStatus() == OrderStatus.CLOSED) {
            throw new BookingAlreadyConfirmedException(orderID);
        }

        booking.updateOrderStatus(OrderStatus.CLOSED);
        bookingRepository.save(booking);
    }

    // add employee to an order. Just if the Service wor on order -> change status to ONTHEWAY
    @Transactional(rollbackFor = {BookingNotFoundException.class, EmployeeNotFoundException.class, BookingAlreadyOnTheWayException.class})
    public Booking addEmployeeToBooking(Long employeeId, Long orderId) throws BookingNotFoundException, EmployeeNotFoundException, BookingAlreadyOnTheWayException {
        Booking booking = bookingRepository
                .findById(orderId)
                .orElseThrow(() -> new BookingNotFoundException(orderId));

        Employee employee = employeeRepository
                .findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        if (booking.getOrderStatus() == OrderStatus.ONTHEWAY) {
            throw new BookingAlreadyOnTheWayException(orderId);
        }

        booking.updateOrderStatus(OrderStatus.ONTHEWAY);
        booking.addEmployee(employee);
        return bookingRepository.save(booking);
    }

    @Transactional(rollbackFor = {ItemNotFoundException.class, SeatNotFoundException.class})
    public Booking createBooking(BookingCreateDTO bookingCreateDTO) throws ItemNotFoundException, SeatNotFoundException {
        Long itemID = bookingCreateDTO.getItem_id();
        Long seatID = bookingCreateDTO.getSeat_id();

        Item item = itemRepository
                .findById(itemID)
                .orElseThrow(() -> new ItemNotFoundException(itemID));
        Seat seat = seatRepository
                .findById(seatID)
                .orElseThrow(() -> new SeatNotFoundException(seatID));

        Booking booking = new Booking(bookingCreateDTO.getAmount(), seat, item);
        return bookingRepository.save(booking);
    }

    public String isValidToken(String token) {
        return token;
    }

    @Transactional(rollbackFor = {AuthentificationException.class})
    public Object authentificate(String qrToken) throws AuthentificationException {
        Optional<Employee> employee = employeeRepository.findByQrtoken(qrToken);
        Optional<Seat> seat = seatRepository.findByQrtoken(qrToken);

        if(employee.isPresent()){
            return employee;
        }
        else if(seat.isPresent()){
            return seat;
        }else
            throw new AuthentificationException(qrToken);
    }

    public String getToken(Qrtoken qrtoken) {
        return qrtoken.getToken();
    }
}
