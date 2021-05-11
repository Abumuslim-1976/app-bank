package uz.pdp.appbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appbank.entity.Role;
import uz.pdp.appbank.entity.enums.RoleName;

public interface RoleRepository extends JpaRepository<Role,Integer> {

    Role findByRoleName(RoleName roleName);
}
