package CoffeeApp.storageservice.models.ingredient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ingredients")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id", nullable = false)
    private Integer id;


    @Column(name = "name", nullable = false, length = 100)
    private String name;


    @Column(name = "cost_per_one_kilo", nullable = false)
    private Float costPerOneKilo;


    @Column(name = "quantity_in_stock", nullable = false)
    private Float quantityInStock;

    @OneToMany(mappedBy = "ingredient", orphanRemoval = true)
    @JsonBackReference
    private Set<IngredientInDrink> drinks = new LinkedHashSet<>();

    @OneToMany(mappedBy = "ingredient")
    private List<IngredientInAcceptance> acceptances;

    @OneToMany(mappedBy = "ingredient")
    private List<IngredientInWriteOff> writeOffs;

    public Ingredient(String name, Float costPerOneKilo, Float quantityInStock) {
        this.name = name;
        this.costPerOneKilo = costPerOneKilo;
        this.quantityInStock = quantityInStock;
    }


}