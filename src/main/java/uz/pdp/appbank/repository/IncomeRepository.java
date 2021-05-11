package uz.pdp.appbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appbank.entity.Income;

public interface IncomeRepository extends JpaRepository<Income,Integer> {
}
