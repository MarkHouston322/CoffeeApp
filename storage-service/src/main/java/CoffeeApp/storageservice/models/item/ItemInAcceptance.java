package CoffeeApp.storageservice.models.item;

import CoffeeApp.storageservice.models.Acceptance;
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
@Table(name = "items_in_acceptance")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class ItemInAcceptance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "items_in_acceptance_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "acceptance_id", referencedColumnName = "acceptance_id")
    private Acceptance acceptance;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    private Item item;

    @Column(name = "quantity")
    private Float quantity;

    public ItemInAcceptance(Acceptance acceptance, Item item, Float quantity) {
        this.acceptance = acceptance;
        this.item = item;
        this.quantity = quantity;
    }
}
