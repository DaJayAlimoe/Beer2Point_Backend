package server.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import server.domain.datatypes.BookingStatus;
import server.domain.entities.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, BookingRepositoryCustom {
    Optional<Booking> findOrderByEmployee(Long employeeId);
    List<Booking> findOrdersByEmployee(Long employeeId);
    List<Booking> findFirst10ByStatusOrderByCreatedOn(BookingStatus status);
    Optional<Booking> findByEmployee_QrtokenToken(String token);
    List<Booking> findFirst30ByEmployee_QrtokenTokenOrderByCreatedOn(String token);
    List<Booking> findFirst10ByActiveAtGreaterThanEqualAndStatusOrderByCreatedOn(LocalDateTime currentDT, BookingStatus status);
    @Modifying
    @Query("UPDATE Booking b SET b.position = b.position - 1 WHERE b.status = :status")
    int decreasePositions(@Param("status") BookingStatus status);
    long countByStatus(BookingStatus status);
}
