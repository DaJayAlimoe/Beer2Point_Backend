package server.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.domain.entities.Seat;

import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    Optional<Seat> findByQrtokenToken(String token);
    Optional<Seat> findById(Long seatId);
    Optional<Seat> findByIdAndQrtokenToken(Long seatId,String token);
}
