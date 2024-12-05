package CoffeeApp.storageservice.models;

import CoffeeApp.storageservice.models.ingredient.IngredientInWriteOff;
import CoffeeApp.storageservice.models.item.ItemInWriteOff;
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
@Table(name = "write_off")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class WriteOff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "write_off_id")
    private Integer id;

    @Column(name = "write_off_date")
    private LocalDateTime date;

    @OneToMany(mappedBy = "writeOff")
    private List<IngredientInWriteOff> ingredients;

    @OneToMany(mappedBy = "writeOff")
    private List<ItemInWriteOff> items;

    @ManyToOne
    @JoinColumn(name = "acceptance_id", referencedColumnName = "acceptance_id")
    private Acceptance acceptance;

    @Column(name = "reason")
    private String reason;

    @Column(name = "total")
    private Float total;

    public WriteOff(LocalDateTime date, Acceptance acceptance, String reason, Float total) {
        this.date = date;
        this.acceptance = acceptance;
        this.reason = reason;
        this.total = total;
    }
}
