package server.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import server.domain.dtos.EmployeeCreateDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ApiModelProperty(required = true)
    private String name;

    @ApiModelProperty
    private Date validTimeStamp;

    // just if its a collection
//    @OneToMany(mappedBy = "employee")
//    private Booking booking;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qrtoken_id", nullable = false)
    private Qrtoken qrtoken;

    public Employee(String name, Qrtoken qrtoken) {
        this.name = name;
        this.validTimeStamp = new Date();
        this.qrtoken = qrtoken;
    }

    public static Employee of(EmployeeCreateDTO employeeCreateDTO) {
        return new Employee(
                employeeCreateDTO.getLastName(),
                employeeCreateDTO.getQrtoken());
    }
}