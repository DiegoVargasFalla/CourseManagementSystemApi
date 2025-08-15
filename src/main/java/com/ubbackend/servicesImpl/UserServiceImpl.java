package com.ubbackend.servicesImpl;

import com.ubbackend.DTOs.UserEntityDTO;
import com.ubbackend.Exceptions.UserExistException;
import com.ubbackend.enumerations.ERol;
import com.ubbackend.model.AccessCodeEntity;
import com.ubbackend.model.RolEntity;
import com.ubbackend.model.UserEntity;
import com.ubbackend.repository.AccessCodeRepository;
import com.ubbackend.repository.UserRepository;
import com.ubbackend.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AccessCodeRepository accessCodeRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, AccessCodeRepository accessCodeRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accessCodeRepository = accessCodeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean createUser(UserEntityDTO userEntityDTO) throws Exception {

        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(userEntityDTO.getUsername());

        if(optionalUserEntity.isPresent()) {
            throw new UserExistException("User already exist");
        } else if(userEntityDTO.getAccessCode() != null) {
            Optional<AccessCodeEntity> accessCodeExisting = accessCodeRepository.findByCode(userEntityDTO.getAccessCode());
            if(accessCodeExisting.isPresent() ) {
                AccessCodeEntity accessCodeEntity = accessCodeExisting.get();

                if(accessCodeEntity.getActive()) {
                    accessCodeEntity.setActive(false);

                    RolEntity rolEntity = new RolEntity();

                    if(accessCodeEntity.getRoleType().toString().equals("SUPER_ADMIN")) {
                        rolEntity.setRole(ERol.SUPER_ADMIN);
                    } else if(accessCodeEntity.getRoleType().toString().equals("ADMIN")) {
                        rolEntity.setRole(ERol.ADMIN);
                    } else if(accessCodeEntity.getRoleType().toString().equals("MODERATOR")) {
                        rolEntity.setRole(ERol.MODERATOR);
                    }

                    Set<RolEntity> roles = new HashSet<>();
                    roles.add(rolEntity);

                    UserEntity userEntity = new UserEntity();
                    userEntity.setEmail(userEntityDTO.getUsername());
                    userEntity.setPassword(passwordEncoder.encode(userEntityDTO.getPassword()));
                    userEntity.setDni(userEntityDTO.getDni());
                    userEntity.setRoles(roles);

                    userRepository.save(userEntity);
                }
                else {
                    throw new UserExistException("Access code has expired");
                }
            }
        }  else {
            return false;
        }
        return true;
    }
}
