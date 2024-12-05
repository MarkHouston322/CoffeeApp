package CoffeeApp.storageservice.models;

import CoffeeApp.storageservice.models.ingredient.IngredientInDrink;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "drinks")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Drink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drink_id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotNull(message = "Drink price should not be empty") //, groups = SecondValidation.class
    @Min(value = 1,message = "Drink price should be greater than one ruble") //, groups = SecondValidation.class
    @Column(name = "price", nullable = false)
    private Integer price;

    @NotNull(message = "Drink cost price should not be empty") //, groups = SecondValidation.class
    @Min(value = 1,message = "Drink cost price should be greater than one ruble") //, groups = SecondValidation.class
    @Column(name = "cost_price", nullable = false)
    private Float costPrice;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    @Column(name = "sold_quantity", nullable = false)
    private Integer soldQuantity;

    @Column(name = "write_off_quantity", nullable = false)
    private Integer writeOffQuantity;

    @OneToMany(mappedBy = "drink", orphanRemoval = true)
    @NotNull(message = "Drink should contain ingredients")
    private Set<IngredientInDrink> ingredients = new LinkedHashSet<>();

    @Column(name = "surcharge_ratio", nullable = false)
    private Float surchargeRatio;

    public Drink(String name, Integer price, Float costPrice, Category category, Integer soldQuantity, Integer writeOffQuantity, Float surchargeRatio) {
        this.name = name;
        this.price = price;
        this.costPrice = costPrice;
        this.category = category;
        this.soldQuantity = soldQuantity;
        this.writeOffQuantity = writeOffQuantity;
        this.surchargeRatio = surchargeRatio;
    }
}