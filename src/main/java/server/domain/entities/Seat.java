package server.domain.entities;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ApiModelProperty(required = true)
    private int seatNr;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "qrtoken_id", nullable = false)
    @Setter(AccessLevel.NONE)
    private Qrtoken qrtoken;
}
