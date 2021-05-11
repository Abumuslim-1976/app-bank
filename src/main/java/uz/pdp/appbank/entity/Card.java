package uz.pdp.appbank.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.appbank.entity.enums.BankName;
import uz.pdp.appbank.entity.enums.PlasticType;
import uz.pdp.appbank.entity.template.AbstractEntity;

import javax.persistence.*;
import java.time.LocalDate;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Card extends AbstractEntity {

    private double balance;                                 // karta ichidagi pul

    @Column(nullable = false, unique = true)
    private String specialNumber;                           // card ning 16 xonalik maxsus raqami

    @Enumerated(value = EnumType.STRING)
    private BankName bankName;                              // qaysi bankka tegishli ekanligi

    private String cvvCode;                                 // 3 xonali cvv kodi

    private LocalDate localDate;                            // card ni amal qilish muddati

    @Column(nullable = false)
    private Integer specialCode;                             // 4 xonali maxsus kod

    @Enumerated(value = EnumType.STRING)
    private PlasticType plasticType;                        // plastik turi (HUMO , UZCARD , VISA)

    @OneToOne(optional = false)
    private User user;                                      // qaysi mijozga tegishli ekanligi

    private boolean active = true;                          // kartaning holati

}
