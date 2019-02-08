package server.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

@Entity
@Data
@NoArgsConstructor//(access = AccessLevel.PRIVATE)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ApiModelProperty(required = true)
    @Setter(AccessLevel.PRIVATE)
    private int seatNr;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qrtoken_id", nullable = false)
    private Qrtoken qrtoken;

    public Seat(int seatNr, Qrtoken qrtoken) {
        this.seatNr = seatNr;
        this.qrtoken = qrtoken;
    }
}
