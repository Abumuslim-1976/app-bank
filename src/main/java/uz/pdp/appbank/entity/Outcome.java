package uz.pdp.appbank.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Outcome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private double commission;

    @Column(nullable = false)
    private double amount;

    @ManyToOne
    private Bankomat toBankomat;

    @ManyToOne
    private Card fromCard;

    @CreationTimestamp
    private Timestamp timestamp;

}
