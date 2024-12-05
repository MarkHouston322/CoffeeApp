package CoffeeApp.storageservice.models.ingredient;

import CoffeeApp.storageservice.models.WriteOff;
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
@Table(name = "ingredients_in_write_off")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class IngredientInWriteOff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredients_in_write_off_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "write_off_id", referencedColumnName = "write_off_id")
    private WriteOff writeOff;

    @ManyToOne
    @JoinColumn(name = "ingredient_id",referencedColumnName = "ingredient_id")
    private Ingredient ingredient;

    @Column(name = "quantity")
    @NotNull
    private Float quantity;

    public IngredientInWriteOff(WriteOff writeOff, Ingredient ingredient, Float quantity) {
        this.writeOff = writeOff;
        this.ingredient = ingredient;
        this.quantity = quantity;
    }
}
