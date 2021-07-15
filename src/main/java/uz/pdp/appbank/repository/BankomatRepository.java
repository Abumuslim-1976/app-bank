package uz.pdp.appbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appbank.entity.Bankomat;

import java.util.UUID;

public interface BankomatRepository extends JpaRepository<Bankomat,UUID>{

}
