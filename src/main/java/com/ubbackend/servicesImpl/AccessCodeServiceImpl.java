package com.ubbackend.servicesImpl;

import com.ubbackend.DTOs.CreateAccessCodeDTO;
import com.ubbackend.Exceptions.UserExistException;
import com.ubbackend.enumerations.ERol;
import com.ubbackend.model.AccessCodeEntity;
import com.ubbackend.model.UserEntity;
import com.ubbackend.repository.AccessCodeRepository;
import com.ubbackend.repository.UserRepository;
import com.ubbackend.services.AccessCodeService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccessCodeServiceImpl implements AccessCodeService {

    private final AccessCodeRepository accessCodeRepository;
    UserRepository userRepository;

    public AccessCodeServiceImpl(UserRepository userRepository, AccessCodeRepository accessCodeRepository) {
        this.userRepository = userRepository;
        this.accessCodeRepository = accessCodeRepository;
    }

    @Override
    public Optional<Long> generateAccessCode(CreateAccessCodeDTO createAccessCodeDTO) throws Exception {

        Optional<UserEntity> userExisting = userRepository.findByEmail(createAccessCodeDTO.getEmailCreator());

        if(userExisting.isPresent()) {

            double code = 100000 + Math.random() * 900000;

            AccessCodeEntity accessCodeEntity = new AccessCodeEntity();
            accessCodeEntity.setCode(Math.round(code));
            accessCodeEntity.setActive(true);
            accessCodeEntity.setCreator(userExisting.get());

            if(createAccessCodeDTO.getRolType().equals(ERol.SUPER_ADMIN.toString())) {
                accessCodeEntity.setRoleType(ERol.SUPER_ADMIN);
            } else if (createAccessCodeDTO.getRolType().equals(ERol.ADMIN.toString())) {
                accessCodeEntity.setRoleType(ERol.ADMIN);
            } else if (createAccessCodeDTO.getRolType().equals(ERol.MODERATOR.toString())) {
                accessCodeEntity.setRoleType(ERol.MODERATOR);
            }

            accessCodeRepository.save(accessCodeEntity);
            return Optional.of(accessCodeEntity.getCode());
        } else {
            throw new UserExistException("Access code couldn't be create");
        }
    }
}
