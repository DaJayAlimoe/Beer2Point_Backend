package server.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.domain.entities.Employee;
import server.domain.entities.Qrtoken;

import java.util.Optional;

@Repository
public interface QrtokenRepository extends JpaRepository<Qrtoken, Long> {
    Optional<Qrtoken> findByToken(String token);
}
