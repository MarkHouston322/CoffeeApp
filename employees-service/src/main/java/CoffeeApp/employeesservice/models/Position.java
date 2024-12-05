package CoffeeApp.employeesservice.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "positions")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "salary")
    private Float salary;

    @Column(name = "royalty")
    private Float royalty;

    @OneToMany(mappedBy = "position")
    private List<Employee> employees;

    public Position(String name, Float salary, Float royalty) {
        this.name = name;
        this.salary = salary;
        this.royalty = royalty;
    }
}
