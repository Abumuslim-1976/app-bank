package uz.pdp.appbank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.appbank.entity.User;
import uz.pdp.appbank.entity.enums.RoleName;
import uz.pdp.appbank.payload.ApiResponse;
import uz.pdp.appbank.payload.LoginDto;
import uz.pdp.appbank.payload.RegisterDto;
import uz.pdp.appbank.repository.CardRepository;
import uz.pdp.appbank.repository.RoleRepository;
import uz.pdp.appbank.repository.UserRepository;
import uz.pdp.appbank.security.JwtProvider;

import java.util.Collections;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    AuthService authService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username + " topilmadi"));
    }

    public UserDetails loadCardBySpecialNumber(String specialNumber) throws UsernameNotFoundException {
        return cardRepository.findBySpecialNumber(specialNumber).orElseThrow(() -> new UsernameNotFoundException(specialNumber + " topilmadi"));
    }


    // ----------- Registratsiyadan o`tish -----------
    public ApiResponse registerUser(RegisterDto registerDto) {
        try {
            User user = new User();
            user.setFirstName(registerDto.getFirstName());
            user.setLastName(registerDto.getLastName());
            user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            user.setEmail(registerDto.getEmail());
            user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.CARD_WORKER)));
            userRepository.save(user);
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
