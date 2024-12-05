package CoffeeApp.sellingservice.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer id;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @OneToMany(mappedBy = "order")
    private List<GoodInOrder> drinks;

    @Column(name = "total")
    private Integer total;

    @Column(name = "discount")
    private Integer discount;

    @Column(name = "total_with_dsc")
    private Integer totalWithDsc;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_id", referencedColumnName = "method_id")
    private PaymentMethod paymentMethod;

    @Column(name = "session_id")
    private Integer sessionId;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "customer_purchase_confirmation")
    private Boolean customerPurchaseConfirmation;

    @Column(name = "employee_name")
    private String employeeName;


//    public Order(LocalDateTime  orderDate, Integer total, Integer discount, Integer totalWithDsc) {
//        this.orderDate = orderDate;
//        this.total = total;
//        this.discount = discount;
//        this.totalWithDsc = totalWithDsc;
//    }


    public Order(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
