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

    /**
     * metodo para obtener todos los codigos de acceso generados
     * @return lista de los codigos de acceso existentes
     */
    @Override
    public List<AccessCodeEntity> getAllAccessCode() {
        List<AccessCodeEntity> accessCodeEntities = accessCodeRepository.findAll();

        for(AccessCodeEntity accessCodeEntity : accessCodeEntities) {
            accessCodeEntity.getCreator().setPassword("");
        }
        return accessCodeEntities;
    }

    /**
     * metodo para generar un codigo de acceso
     * @param accessCodeCreatedDTO atriutos necesarios para generar un codigo de acceso
     * @return Optional con el codigo de 6 digitos
     * @throws Exception si el usuario que esta generando el codigo no existe lanzara
     * una axcepcion informando que el codigo no se pudo crear
     */
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
            }

            accessCodeRepository.save(accessCodeEntity);
            return Optional.of(accessCodeEntity.getCode());
        } else {
            throw new UserExistException("Access code couldn't be create");
        }
    }

    /**
     * metodo para cancelar el codigo de acceso y que no se pueda usar
     * @param accessCodeId atributos para cancelar el codigo de acceso
     * @return codigo de acceso cancelado, el atributo active deve estar en false
     * @throws Exception si el codigo no existe retornara un Optional vacio
     */
    @Override
    public Optional<AccessCodeEntity> cancelAccessCode(Long accessCodeId) {
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
