package com.ubbackend.servicesImpl;

import com.ubbackend.DTO.AccessCodeCreatedDTO;
import com.ubbackend.exception.UserExistException;
import com.ubbackend.enumeration.ERol;
import com.ubbackend.model.AccessCodeEntity;
import com.ubbackend.model.UserEntity;
import com.ubbackend.repository.AccessCodeRepository;
import com.ubbackend.repository.UserRepository;
import com.ubbackend.services.AccessCodeService;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public List<AccessCodeEntity> getAllAccessCode() {
        List<AccessCodeEntity> accessCodeEntities = accessCodeRepository.findAll();

        for(AccessCodeEntity accessCodeEntity : accessCodeEntities) {
            accessCodeEntity.getCreator().setPassword("");
        }
        return accessCodeEntities;
    }

    @Override
    public Optional<Long> generateAccessCode(AccessCodeCreatedDTO accessCodeCreatedDTO) throws Exception {

        Optional<UserEntity> userExisting = userRepository.findByEmail(accessCodeCreatedDTO.getEmailCreator());

        if(userExisting.isPresent()) {

            double code = 100000 + Math.random() * 900000;

            AccessCodeEntity accessCodeEntity = new AccessCodeEntity();
            accessCodeEntity.setCode(Math.round(code));
            accessCodeEntity.setActive(true);
            accessCodeEntity.setCreator(userExisting.get());

            if(accessCodeCreatedDTO.getRolType().equals(ERol.SUPER_ADMIN.toString())) {
                accessCodeEntity.setRoleType(ERol.SUPER_ADMIN);
            } else if (accessCodeCreatedDTO.getRolType().equals(ERol.ADMIN.toString())) {
                accessCodeEntity.setRoleType(ERol.ADMIN);
            } else if (accessCodeCreatedDTO.getRolType().equals(ERol.MODERATOR.toString())) {
                accessCodeEntity.setRoleType(ERol.MODERATOR);
            }

            accessCodeRepository.save(accessCodeEntity);
            return Optional.of(accessCodeEntity.getCode());
        } else {
            throw new UserExistException("Access code couldn't be create");
        }
    }

    @Override
    public Optional<AccessCodeEntity> cancelAccessCode(Long accessCodeId) throws Exception {
        Optional<AccessCodeEntity> accessCodeEntity = accessCodeRepository.findById(accessCodeId);
        if(accessCodeEntity.isPresent()) {
            accessCodeEntity.get().setActive(false);
            AccessCodeEntity updatedAccessCodeEntity = accessCodeRepository.save(accessCodeEntity.get());
            updatedAccessCodeEntity.getCreator().setPassword("");
            return Optional.of(updatedAccessCodeEntity);
        }
        return Optional.empty();
    }
}
