package CoffeeApp.storageservice.models.item;

import CoffeeApp.storageservice.models.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = " item_id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Integer price;

    @Column(name = "surcharge_ratio")
    private Float surchargeRatio;

    @Column(name = "cost_price")
    private Float costPrice;

    @Column(name = "quantity_in_stock")
    private Float quantityInStock;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    @OneToMany(mappedBy = "item")
    private List<ItemInAcceptance> acceptances;

    @OneToMany(mappedBy = "item")
    private List<ItemInWriteOff> writeOffs;

    @OneToMany(mappedBy = "item")
    private List<ItemInFridge> unitsInFridge;

    public Item(String name, Float surchargeRatio, Float costPrice, Category category) {
        this.name = name;
        this.surchargeRatio = surchargeRatio;
        this.costPrice = costPrice;
        this.category = category;
    }
}
