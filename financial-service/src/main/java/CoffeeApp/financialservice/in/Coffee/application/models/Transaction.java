package CoffeeApp.financialservice.in.Coffee.application.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "transactions")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Integer id;

    @Column(name = "date")
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "type", referencedColumnName = "transaction_type_id")
    private TransactionType type;

    @Column(name = "sum")
    private Integer sum;

    @ManyToOne
    @JoinColumn(name = "session_id", referencedColumnName = "session_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Session session;

    public Transaction(TransactionType type, Integer sum) {
        this.type = type;
        this.sum = sum;
    }

    public Transaction(LocalDateTime date, TransactionType type, Integer sum, Session session) {
        this.date = date;
        this.type = type;
        this.sum = sum;
        this.session = session;
    }
}
