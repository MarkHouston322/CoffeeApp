package CoffeeApp.storageservice.models.item;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "items_in_fridge")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class ItemInFridge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_in_fridge_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    private Item item;

    @Column(name = "place_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date placeDate;

    @Column(name = "storage_time")
    private Long storageTime;

    @Column(name = "is_sold")
    private Boolean isSold;

    @Column(name = "sold_date")
    private LocalDateTime soldDate;

    @Column(name = "expired")
    private Boolean expired;

    public ItemInFridge(Item item, Long storageTime) {
        this.item = item;
        this.storageTime = storageTime;
    }
}
