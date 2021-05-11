package uz.pdp.appbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appbank.entity.Outcome;

public interface OutcomeRepository extends JpaRepository<Outcome,Integer> {
}
