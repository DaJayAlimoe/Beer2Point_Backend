package server.domain.repositories;

import server.domain.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.domain.entities.Qrtoken;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByQrtokenToken(String token);
    Optional<Employee> findById(Long employeeId);
}
