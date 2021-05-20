package uz.pdp.appbank.load;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.appbank.entity.Role;
import uz.pdp.appbank.entity.User;
import uz.pdp.appbank.entity.enums.RoleName;
import uz.pdp.appbank.repository.RoleRepository;
import uz.pdp.appbank.repository.UserRepository;

import java.util.Collections;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;



    @Value("${spring.datasource.initialization-mode}")
    private String initialMode;


    @Override
    public void run(String... args) throws Exception {

        if (initialMode.equals("always")) {

            roleRepository.save(new Role(
                    RoleName.DIRECTOR
            ));

            roleRepository.save(new Role(
                    RoleName.CLIENT
            ));

            userRepository.save(new User(
                    "Akbarov",
                    "Islom",
                    passwordEncoder.encode("1234"),
                    "akbarjon@gmail.com",
                    Collections.singleton(roleRepository.findByRoleName(RoleName.DIRECTOR))
            ));

            userRepository.save(new User(
                    "Ismatov",
                    "Nurali",
                    passwordEncoder.encode("222333"),
                    "nurali123@gmail.com",
                    Collections.singleton(roleRepository.findByRoleName(RoleName.CLIENT))
            ));

        }

    }


}
