package server.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.domain.datatypes.Item;
import server.domain.entities.Booking;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, BookingRepositoryCustom {
    Optional<Booking> findOrderByEmployee(Long employeeId);
    List<Booking> findOrdersByEmployee(Long employeeId);
    Optional<Booking> findOrdersByItemName(Item itemName);
}
