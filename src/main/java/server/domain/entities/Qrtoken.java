package server.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Qrtoken {

    public Qrtoken(String token) {
        this.token = token;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ApiModelProperty(required = true)
    @Setter(AccessLevel.NONE)
    private String token;

    @ApiModelProperty(required = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "qrtoken")
    @JsonIgnore
    private Employee employee;

    @ApiModelProperty(required = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "qrtoken")
    @JsonIgnore
    private Seat seat;

}
