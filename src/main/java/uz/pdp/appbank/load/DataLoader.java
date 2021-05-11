package uz.pdp.appbank.load;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.appbank.entity.Staff;
import uz.pdp.appbank.entity.User;
import uz.pdp.appbank.entity.enums.RoleName;
import uz.pdp.appbank.repository.RoleRepository;
import uz.pdp.appbank.repository.StaffRepository;
import uz.pdp.appbank.repository.UserRepository;

import java.util.Collections;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    UserRepository userRepository;



    @Value("${spring.datasource.initialization-mode}")
    private String initialMode;


    @Override
    public void run(String... args) throws Exception {

        if (initialMode.equals("always")) {
            Staff staff = new Staff();
            staff.setFirstName("Akbarov");
            staff.setLastName("Islom");
            staff.setPassword(passwordEncoder.encode("1234"));
            staff.setEmail("akbarjon@gmail.com");
            staff.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.DIRECTOR)));
            staffRepository.save(staff);

            User user = new User();
            user.setFirstName("Ismatov");
            user.setLastName("Nurali");
            user.setPassword(passwordEncoder.encode("222333"));
            user.setEmail("nurali123@gmail.com");
            user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.CLIENT)));
            userRepository.save(user);
        }



    }


}
