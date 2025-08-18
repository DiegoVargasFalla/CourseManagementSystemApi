package com.ubbackend.servicesImpl;

import com.ubbackend.enumeration.ERol;
import com.ubbackend.model.RolEntity;
import com.ubbackend.model.UserEntity;
import com.ubbackend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if(userRepository.count() == 0) {
            UserEntity superAdmin = new UserEntity();

            superAdmin.setEmail("superadmin@gmail.com");
            superAdmin.setDni(123456789L);
            superAdmin.setPassword(passwordEncoder.encode("123456"));

            RolEntity role = new RolEntity();
            role.setRole(ERol.SUPER_ADMIN);
            Set<RolEntity> roles = new HashSet<>();
            roles.add(role);
            superAdmin.setRoles(roles);
            userRepository.save(superAdmin);
        }
    }
}
