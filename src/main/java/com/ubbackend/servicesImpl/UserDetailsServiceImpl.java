package com.ubbackend.servicesImpl;

import com.ubbackend.model.UserEntity;
import com.ubbackend.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetailsService{
    private final UserRepository userRepository;

    public UserDetailsImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<UserEntity> user = userRepository.findByEmail(email);

        if(user.isPresent()) {
            UserEntity userEntity = user.get();

            Collection<? extends GrantedAuthority> authorities = userEntity.getRoles()
                    .stream()
                    .map( role -> new SimpleGrantedAuthority("ROLE_".concat(role.getRole().name())))
                    .collect(Collectors.toSet());

            return new User(
                    userEntity.getEmail(),
                    userEntity.getPassword(),
                    true,
                    true,
                    true,
                    true,
                    authorities);

        } else {
            throw new UsernameNotFoundException("User " + email + " not exist");
        }
    }
}
