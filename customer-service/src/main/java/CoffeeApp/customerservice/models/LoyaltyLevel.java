package CoffeeApp.customerservice.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "loyalty_levels")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class LoyaltyLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = " level_id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "edge")
    private Integer edge;

    @Column(name = " discount_percentage")
    private Float  discountPercentage;

    @OneToMany(mappedBy = "loyaltyLevel")
    private List<Customer> customers;

    public LoyaltyLevel(String name, Integer edge, Float discountPercentage) {
        this.name = name;
        this.edge = edge;
        this.discountPercentage = discountPercentage;
    }
}
