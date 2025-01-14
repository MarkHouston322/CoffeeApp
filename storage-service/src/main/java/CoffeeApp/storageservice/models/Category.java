package CoffeeApp.storageservice.models;

import CoffeeApp.storageservice.models.item.Item;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "category")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Integer id;


    @Column(name = "category_name", nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Drink> drinks;

    @OneToMany(mappedBy = "category")
    private List<Item> items;

    public Category(String name, List<Drink> drinks) {
        this.name = name;
        this.drinks = drinks;
    }

    public Category(String name) {
        this.name = name;
    }
}