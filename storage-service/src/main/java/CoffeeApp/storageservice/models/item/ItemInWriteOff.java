package CoffeeApp.storageservice.models.item;

import CoffeeApp.storageservice.models.WriteOff;
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
@Table(name = "items_in_write_off")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class ItemInWriteOff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "items_in_write_off_id")
    private Integer id;


    @ManyToOne
    @JoinColumn(name = "write_off_id", referencedColumnName = "write_off_id")
    private WriteOff writeOff;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    private Item item;

    @Column(name = "quantity")
    private Float quantity;

    public ItemInWriteOff(WriteOff writeOff, Item item, Float quantity) {
        this.writeOff = writeOff;
        this.item = item;
        this.quantity = quantity;
    }
}
