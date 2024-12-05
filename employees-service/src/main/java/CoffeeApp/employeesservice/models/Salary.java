package CoffeeApp.employeesservice.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "salary")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Salary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "salary_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
    private Employee employee;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "salary")
    private Float salary;

    @Column(name = "royalty")
    private Float royalty;

    @Column(name = "bonus_value")
    private Integer bonus;

    @Column(name = "total")
    private Float total;

    @Column(name = "revenue")
    private Integer revenue;

    @Column(name = "salary_session_is_closed")
    private Boolean isClosed;

    public Salary(Employee employee, LocalDateTime date, Float salary, Float royalty,Integer bonus, Float total, Integer revenue, Boolean isClosed) {
        this.employee = employee;
        this.date = date;
        this.salary = salary;
        this.royalty = royalty;
        this.bonus = bonus;
        this.total = total;
        this.revenue = revenue;
        this.isClosed = isClosed;
    }
}
