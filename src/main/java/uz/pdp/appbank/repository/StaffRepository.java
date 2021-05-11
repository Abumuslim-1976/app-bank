package uz.pdp.appbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appbank.entity.Staff;

import javax.validation.constraints.Email;
import java.util.Optional;
import java.util.UUID;

public interface StaffRepository extends JpaRepository<Staff, UUID> {

    boolean existsByEmail(@Email String email);

    Optional<Staff> findByEmail(@Email String email);

}
