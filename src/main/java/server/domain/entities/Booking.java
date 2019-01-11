package server.domain.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import server.domain.datatypes.OrderStatus;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter(AccessLevel.PRIVATE)
    private final Date createdOn = new Date();

    @Setter(AccessLevel.NONE)
    private OrderStatus orderStatus = OrderStatus.PREORDERED;

    @Size(min = 1, max = 5)
    private int amount;

    // versioncontroll and config for DB
    @Version
    private Long version;

    private Date lastUpdatedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @Setter(AccessLevel.NONE)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seat_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Seat seat;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Setter(AccessLevel.NONE)
    private Item item;

    public Booking(@Size(min = 1, max = 5) int amount, Seat seat, Item item) {
        this.amount = amount;
        this.seat = seat;
        this.item = item;
        this.lastUpdatedOn = new Date();
    }

    public void updateOrderStatus(OrderStatus newStatus) {
        orderStatus = orderStatus.transition(newStatus);
        lastUpdatedOn = new Date();
    }

    public void addEmployee(Employee newEmployee){
        this.employee = newEmployee;
        lastUpdatedOn = new Date();
    }

//    public String getCreatedOnModified(){
//        LocalDate time = this.getCreatedOn().toInstant()
//                .atZone(ZoneId.systemDefault())
//                .toLocalDate();
////        LocalDate today = LocalDate..now( ZoneId.of( "Europe/Paris" ) ) ;
//        return time.toString();
//    }
}
