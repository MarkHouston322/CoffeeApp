package CoffeeApp.sellingservice.models;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "goods_in_order")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class GoodInOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goods_in_order_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private Order order;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "good_name")
    private String goodName;

    @Column(name = "good_price")
    private Integer goodPrice;

    @Column(name = "sold_confirmation")
    private Boolean soldConfirmation;

    public GoodInOrder(Order order, Integer quantity, String goodName, Integer goodPrice, Boolean soldConfirmation) {
        this.order = order;
        this.quantity = quantity;
        this.goodName = goodName;
        this.goodPrice = goodPrice;
        this.soldConfirmation = soldConfirmation;
    }
}
