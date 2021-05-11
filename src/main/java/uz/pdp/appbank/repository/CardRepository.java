package uz.pdp.appbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appbank.entity.Card;

import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {
}
