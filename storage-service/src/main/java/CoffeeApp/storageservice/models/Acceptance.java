package CoffeeApp.storageservice.models;

import CoffeeApp.storageservice.models.ingredient.IngredientInAcceptance;
import CoffeeApp.storageservice.models.item.ItemInAcceptance;
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
@Table(name = "acceptance")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Acceptance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "acceptance_id")
    private Integer id;

    @Column(name = "acceptance_date")
    private LocalDateTime date;

    @OneToMany(mappedBy = "acceptance")
    private List<IngredientInAcceptance> ingredients;

    @OneToMany(mappedBy = "acceptance")
    private List<ItemInAcceptance> items;

    @Column(name = "comment")
    private String comment;

    @OneToMany(mappedBy = "acceptance")
    private List<WriteOff> writeOffs;

    @Column(name = "total")
    private Float total;
    public Acceptance(LocalDateTime date, String comment, Float total) {
        this.date = date;
        this.comment = comment;
        this.total = total;
    }
}
