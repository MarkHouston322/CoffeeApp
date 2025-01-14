package CoffeeApp.financialservice.in.Coffee.application.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sessions")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Integer id;

    @Column(name = " opening_session_date")
    private LocalDateTime openingDate;

    @Column(name = "employee_name")
    private String employeeUsername;

    @Column(name = "initial_cash"
            ,columnDefinition = "integer default 0")
    private Integer initialCash;

    @Column(name = "cash_inflow"
            ,columnDefinition = "integer default 0")
    private Integer cashInflow;

    @Column(name = "cash_expense"
            ,columnDefinition = "integer default 0")
    private Integer cashExpense;

    @Column(name = "card_Payment"
            ,columnDefinition = "integer default 0")
    private Integer cardPayment;

    @Column(name = "cash_payment"
            ,columnDefinition = "integer default 0")
    private Integer cashPayment;

    @Column(name = "order_quantity"
            ,columnDefinition = "integer default 0")
    private Integer ordersQuantity;

    @Column(name = "total_money"
            ,columnDefinition = "integer default 0")
    private Integer totalMoney;

    @Column(name = "finite_cash"
            ,columnDefinition = "integer default 0")
    private Integer finiteCash;

    @Column(name = "closing_session_date")
    private LocalDateTime closingDate;

    @Column(name = "session_is_closed")
    private Boolean sessionIsClosed;

    @OneToMany(mappedBy = "session")
    private List<Transaction> transactions;

}

