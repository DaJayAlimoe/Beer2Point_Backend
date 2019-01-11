package server.domain.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import server.domain.dtos.EmployeeCreateDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "employee_id", nullable=false)
    private Long id;

    @ApiModelProperty(required = true)
    private String name;

    @ApiModelProperty
    private Date validTimeStamp;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "qrtoken_id", nullable = false)
    private Qrtoken qrtoken;

    public Employee(String lastName) {
        this.validTimeStamp = new Date();
        this.name = lastName;
    }

    public static Employee of(EmployeeCreateDTO employeeCreateDTO) {
        return new Employee(
                employeeCreateDTO.getLastName());
    }
}