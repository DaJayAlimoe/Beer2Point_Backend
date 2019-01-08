package server.domain.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import server.domain.datatypes.Item;
import server.domain.datatypes.OrderStatus;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter(AccessLevel.PRIVATE)
    private final Date createdOn = new Date();

    private Item itemName;

    @Setter(AccessLevel.NONE)
    private OrderStatus orderStatus = OrderStatus.PREORDERED;

    private int amount;

    private String seat;

    // versioncontroll and config for DB
    @Version
    private Long version;

    private Date lastUpdatedOn;

    public Booking(Item itemName, int amount, String seat) {
        this.itemName = itemName;
        this.amount = amount;
        this.seat = seat;
        this.lastUpdatedOn = new Date();
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    @Setter(AccessLevel.NONE)
    @Embedded
    private Employee employee;


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
