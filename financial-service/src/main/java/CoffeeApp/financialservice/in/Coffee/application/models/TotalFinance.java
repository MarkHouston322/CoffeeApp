package CoffeeApp.financialservice.in.Coffee.application.models;

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
@Table(name = "total_financial_flow")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class TotalFinance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "total_cash_flow_id")
    private Integer id;

    @Column(name = "cash_amount")
    private Integer cash;

    @Column(name = "card_amount")
    private Integer card;

    public TotalFinance(Integer cash, Integer card) {
        this.cash = cash;
        this.card = card;
    }
}
