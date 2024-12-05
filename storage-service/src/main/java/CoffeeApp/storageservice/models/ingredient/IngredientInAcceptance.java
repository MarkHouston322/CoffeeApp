package CoffeeApp.storageservice.models.ingredient;

import CoffeeApp.storageservice.models.Acceptance;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ingredients_in_acceptance")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class IngredientInAcceptance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredients_in_acceptance_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "acceptance_id", referencedColumnName = "acceptance_id")
    private Acceptance acceptance;

    @ManyToOne
    @JoinColumn(name = "ingredient_id",referencedColumnName = "ingredient_id")
    private Ingredient ingredient;

    @Column(name = "quantity")
    @NotNull
    private Float quantity;

    public IngredientInAcceptance(Acceptance acceptance, Ingredient ingredient, Float quantity) {
        this.acceptance = acceptance;
        this.ingredient = ingredient;
        this.quantity = quantity;
    }
}
