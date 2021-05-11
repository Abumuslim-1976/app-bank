package uz.pdp.appbank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.appbank.entity.Role;
import uz.pdp.appbank.entity.Staff;
import uz.pdp.appbank.entity.User;
import uz.pdp.appbank.entity.enums.RoleName;
import uz.pdp.appbank.payload.ApiResponse;
import uz.pdp.appbank.payload.LoginDto;
import uz.pdp.appbank.payload.RegisterDto;
import uz.pdp.appbank.repository.RoleRepository;
import uz.pdp.appbank.repository.StaffRepository;
import uz.pdp.appbank.repository.UserRepository;
import uz.pdp.appbank.security.JwtProvider;

import java.util.Collections;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    AuthService authService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Staff> staffOptional = staffRepository.findByEmail(username);
        Optional<User> userOptional = userRepository.findByEmail(username);

        if (staffOptional.isPresent()) return staffOptional.get();
        if (userOptional.isPresent()) return userOptional.get();

        throw new UsernameNotFoundException(username + " topilmadi");
    }


//    public UserDetails loadClientByUsername(String username) throws UsernameNotFoundException {
//        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username + " topilmadi"));
//    }


    // ----------- Registratsiyadan o`tish -----------
    public ApiResponse registerUser(RegisterDto registerDto) {
        try {
            Staff staff = new Staff();
            staff.setFirstName(registerDto.getFirstName());
            staff.setLastName(registerDto.getLastName());
            staff.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            staff.setEmail(registerDto.getEmail());
            staff.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.CARD_WORKER)));
            staffRepository.save(staff);
            return new ApiResponse("Ishchi saqlandi", true);
        } catch (Exception e) {
            return new ApiResponse("Xatolik !!!", false);
        }
    }


    public ApiResponse loginUser(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(),
                    loginDto.getPassword())
            );

            String token = jwtProvider.generateToken(loginDto.getUsername());
            return new ApiResponse("Login to User", true, token);
        } catch (BadCredentialsException e) {
            return new ApiResponse("email or password not found", false);
        }
    }


}
