package server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.domain.datatypes.BookingStatus;
import server.domain.dtos.AuthEmplSeatDTO;
import server.domain.dtos.BookingCreateDTO;
import server.domain.dtos.EmployeeCreateDTO;
import server.domain.dtos.IdDTO;
import server.domain.entities.*;
import server.domain.repositories.*;
import server.exceptions.*;

import javax.naming.AuthenticationException;
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
    public void confirmBooking(Long orderID, String token) throws BookingAlreadyConfirmedException, BookingNotFoundException {

        Booking booking = bookingRepository
                .findById(orderID)
                .orElseThrow(() -> new BookingNotFoundException(orderID));

        if (booking.getStatus() == BookingStatus.CLOSED || booking.getStatus() == BookingStatus.CANCELED) {
            throw new BookingAlreadyConfirmedException(orderID);
        }

        booking.updateOrderStatus(BookingStatus.CLOSED);
        bookingRepository.save(booking);
    }

    // add employee to an order -> change status to ONTHEWAY
    @Transactional(rollbackFor = {BookingNotFoundException.class, BookingAlreadyOnTheWayException.class})
    public void addEmployeeToBooking(Employee employee, Long orderId) throws BookingNotFoundException, BookingAlreadyOnTheWayException {
        Booking booking = bookingRepository
                .findById(orderId)
                .orElseThrow(() -> new BookingNotFoundException(orderId));

        if (booking.getStatus() == BookingStatus.ONTHEWAY) {
            throw new BookingAlreadyOnTheWayException(orderId);
        }
        booking.updateOrderStatus(BookingStatus.ONTHEWAY);
        booking.addEmployee(employee);
        booking.updateETA(5);
        bookingRepository.save(booking);
        bookingRepository.decreasePositions(BookingStatus.PREORDERED);
    }

    @Transactional(rollbackFor = {ItemNotFoundException.class})
    public Booking createBooking(Long item_id, Seat seat, int amount) throws ItemNotFoundException {

        Item item = itemRepository
                .findById(item_id)
                .orElseThrow(() -> new ItemNotFoundException(item_id));
        long eta = calculateETA(5);
        Booking booking = new Booking(amount, seat, item, eta, bookingRepository.countByStatus(BookingStatus.PREORDERED)+1);
        return bookingRepository.save(booking);
    }

    @Transactional(readOnly = true)
    public AuthEmplSeatDTO authenticate(String qrToken) throws TokenAuthenticationException {

        Optional<Qrtoken> optionalQrtoken = qrtokenRepository.findByToken(qrToken);

        if(!optionalQrtoken.isPresent())
            throw new TokenAuthenticationException(qrToken);

        if(optionalQrtoken.get().getEmployee() != null) {
            return new AuthEmplSeatDTO(optionalQrtoken.get().getEmployee());
        }
        else
            return new AuthEmplSeatDTO(optionalQrtoken.get().getSeat());
    }


    @Transactional(readOnly = true)
    public Qrtoken isValidToken(String token) throws TokenAuthenticationException {

        Optional<Qrtoken> optionalQrtoken = qrtokenRepository.findByToken(token);

        if(!optionalQrtoken.isPresent())
            throw new TokenAuthenticationException(token);

        return optionalQrtoken.get();
    }

    @Transactional(readOnly = true)
    public void isValidEmployee(String token) throws EmployeeTokenWrongException {
        Optional<Employee> optionalEmployee = employeeRepository.findByQrtokenToken(token);

        if(!optionalEmployee.isPresent())
            throw new EmployeeTokenWrongException(token);
    }

    @Transactional(readOnly = true)
    public void isValidSeat(String token) throws SeatTokenWrongException {
        Optional<Seat> optionalSeat = seatRepository.findByQrtokenToken(token);

        if(!optionalSeat.isPresent())
            throw new SeatTokenWrongException(token);
    }

    public boolean isEmployeeToken(String token){
        Optional<Employee> optionalEmployee = employeeRepository.findByQrtokenToken(token);

        return (optionalEmployee.isPresent());
    }

    public boolean isSeatToken(String token){
        Optional<Seat> optionalSeat = seatRepository.findByQrtokenToken(token);

        return (optionalSeat.isPresent());
    }


    private long calculateETA(int minuteFactor) {
        long orderPerEmployee = (bookingRepository.countByStatus(BookingStatus.PREORDERED) + 1)/employeeRepository.count();
        return orderPerEmployee * minuteFactor;
    }

}
