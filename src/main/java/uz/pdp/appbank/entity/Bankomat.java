package uz.pdp.appbank.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.appbank.entity.enums.PlasticType;
import uz.pdp.appbank.entity.template.AbstractEntity;

import javax.persistence.*;
import java.util.Set;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Bankomat extends AbstractEntity {

    @Enumerated(value = EnumType.STRING)
    private PlasticType plasticType;                            // qaysi turdagi plastik kartochka(HUMO,UZCARD,VISA)

    @Column(nullable = false)
    private long maxMoney;                                      // bankamatdan max pul yechish miqdori

    @Column(nullable = false)
    private Integer readyMoney;                                     // bankdagi naqd pul miqdori

    private double plasticMoney;                                  // card dan yechib olingan pul saqlanadigan joy

    private double oneHundredThousandCount;                        // yuz ming so`mlik

    private double fiftyThousandCount;                             // ellik ming so`mlik

    private double tenThousandCount;                                // o`n ming so`mlik

    @Column(nullable = false)
    private String address;                                       // bankomatning addresi

}
