package server.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;
import server.domain.datatypes.BookingStatus;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter(AccessLevel.PRIVATE)
    private final Date createdOn = new Date();

    @Setter(AccessLevel.NONE)
    private BookingStatus status = BookingStatus.PREORDERED;

    @Range(min = 0, max = 5)
    private int amount;

    // versioncontroll and config for DB
    @Version
    @Setter(AccessLevel.NONE)
    private Long version;

    @Setter(AccessLevel.PRIVATE)
    private Date lastUpdatedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seat_id", nullable = false)
    @Setter(AccessLevel.NONE)
    private Seat seat;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    @Setter(AccessLevel.NONE)
    private Item item;

    public Booking(@Range(min = 0, max = 5)int amount, Seat seat, Item item) {
        this.amount = amount;
        this.seat = seat;
        this.item = item;
        this.lastUpdatedOn = new Date();
    }

    public void updateOrderStatus(BookingStatus newStatus) {
        status = status.transition(newStatus);
        lastUpdatedOn = new Date();
    }

    public void addEmployee(Employee newEmployee){
        this.employee = newEmployee;
        lastUpdatedOn = new Date();
    }
}
