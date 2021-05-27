package uz.pdp.appbank.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.pdp.appbank.entity.enums.BankName;
import uz.pdp.appbank.entity.enums.PlasticType;
import uz.pdp.appbank.entity.template.AbstractEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Card extends AbstractEntity implements UserDetails {

    private double balance;                                 // karta ichidagi pul

    @Column(nullable = false, unique = true)
    private String specialNumber;                           // card ning 16 xonalik maxsus raqami

    @Enumerated(value = EnumType.STRING)
    private BankName bankName;                              // qaysi bankka tegishli ekanligi

    private String cvvCode;                                 // 3 xonali cvv kodi

    @Column(nullable = false)
    private LocalDate localDate;                            // card ni amal qilish muddati

    @Column(nullable = false)
    private String username;                                // card ning usernamesi

    @Column(nullable = false)
    private Integer specialCode;                             // 4 xonali maxsus kod

    @Enumerated(value = EnumType.STRING)
    private PlasticType plasticType;                        // plastik turi (HUMO , UZCARD , VISA)

    private boolean active = true;                          // kartaning holati

    @ManyToOne
    private Role role;

    private boolean accountNonExpired = true;

    private boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;

    private boolean enabled = false;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(role);
    }

    @Override
    public String getPassword() {
        return this.specialCode.toString();
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
