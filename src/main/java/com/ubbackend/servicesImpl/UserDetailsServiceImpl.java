package com.ubbackend.servicesImpl;

import com.ubbackend.model.UserEntity;
import com.ubbackend.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("-> ingresando a loadByUsername");

        Optional<UserEntity> user = userRepository.findByEmail(email);

        if(user.isPresent()) {

            UserEntity userEntity = user.get();
            Collection<? extends GrantedAuthority> authorities = userEntity.getRoles()
                    .stream()
                    .map( role -> new SimpleGrantedAuthority("ROLE_".concat(role.getRole().name())))
                    .collect(Collectors.toSet());

            for(GrantedAuthority authority : authorities) {
                System.out.println("-> " + authority.getAuthority());
            }

            return new User(
                    userEntity.getEmail(),
                    userEntity.getPassword(),
                    true,
                    true,
                    true,
                    true,
                    authorities
            );

        } else {
            throw new UsernameNotFoundException("User " + email + " not exist");
        }
    }
}
