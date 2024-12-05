package CoffeeApp.customerservice.models;

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
@Table(name = "customers")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "total_purchases")
    private Integer totalPurchases;

    @ManyToOne
    @JoinColumn(name = "loyalty_level", referencedColumnName = "level_id")
    private LoyaltyLevel loyaltyLevel;

    public Customer(String name, String secondName, String mobileNumber, String email) {
        this.name = name;
        this.secondName = secondName;
        this.mobileNumber = mobileNumber;
        this.email = email;
    }
}
