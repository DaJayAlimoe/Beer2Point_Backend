package server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.domain.datatypes.OrderStatus;
import server.domain.dtos.EmployeeCreateDTO;
import server.domain.dtos.IdDTO;
import server.domain.entities.Booking;
import server.domain.entities.Employee;
import server.domain.repositories.BookingRepository;
import server.domain.repositories.EmployeeRepository;
import server.exceptions.*;

import java.util.List;
import java.util.Optional;

@Service
public class LogicalService {

    private final EmployeeRepository employeeRepository;

    private final BookingRepository bookingRepository;

    @Autowired
    public LogicalService(EmployeeRepository employeeRepository, BookingRepository bookingRepository) {  // cyclic dependency resolution; see http://www.baeldung.com/circular-dependencies-in-spring
        this.employeeRepository = employeeRepository;
        this.bookingRepository = bookingRepository;
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
    public void confirmOrder(Long orderID) throws BookingAlreadyConfirmedException, BookingNotFoundException {
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
    public Booking addEmployeeToOrder(Long employeeId, Long orderId) throws BookingNotFoundException, EmployeeNotFoundException, BookingAlreadyOnTheWayException {
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
}
