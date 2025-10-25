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
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
public class AccessCodeServiceImpl implements AccessCodeService {

    private final AccessCodeRepository accessCodeRepository;
    private final MailSenderImpl mailSender;
    UserRepository userRepository;

    public AccessCodeServiceImpl(UserRepository userRepository, AccessCodeRepository accessCodeRepository, MailSenderImpl mailSender) {
        this.userRepository = userRepository;
        this.accessCodeRepository = accessCodeRepository;
        this.mailSender = mailSender;
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
    @Transactional
    public Optional<String> generateAccessCode(AccessCodeCreatedDTO accessCodeCreatedDTO) throws Exception {

        Optional<UserEntity> userExisting = userRepository.findByEmail(accessCodeCreatedDTO.getEmailCreator());

        if(userExisting.isPresent()) {

            double code = 100000 + Math.random() * 900000;

            AccessCodeEntity accessCodeEntity = new AccessCodeEntity();

            accessCodeEntity.setCode(Math.round(code));
            accessCodeEntity.setActive(true);
            accessCodeEntity.setCreator(userExisting.get());
            accessCodeEntity.setEmailRecipient(accessCodeCreatedDTO.getEmailRecipient());

            if(accessCodeCreatedDTO.getRolType().equals(ERol.SUPER_ADMIN.toString())) {
                accessCodeEntity.setRoleType(ERol.SUPER_ADMIN);
            } else if (accessCodeCreatedDTO.getRolType().equals(ERol.ADMIN.toString())) {
                accessCodeEntity.setRoleType(ERol.ADMIN);
            }

            accessCodeRepository.save(accessCodeEntity);


            mailSender.sendMail(accessCodeCreatedDTO.getEmailRecipient(), "Codigo de registro", accessCodeEntity.getCode().toString(), "http://localhost/register:8080?ac=" + URLEncoder.encode(accessCodeEntity.getCode().toString(), StandardCharsets.UTF_8));
            return Optional.of("AccessCode successfully created");
        }
        return Optional.empty();
    }

    /**
     * metodo para cancelar el codigo de acceso y que no se pueda usar
     * @param accessCode atributos para cancelar el codigo de acceso, se cancela
     * por medio del mismo codigo que se envia no por id
     * @return codigo de acceso cancelado, el atributo active debe estar en false
     */
    @Override
    public Optional<AccessCodeEntity> cancelAccessCode(Long accessCode) {
        Optional<AccessCodeEntity> accessCodeEntity = accessCodeRepository.findByCode(accessCode);
        if(accessCodeEntity.isPresent()) {
            accessCodeEntity.get().setActive(false);
            AccessCodeEntity updatedAccessCodeEntity = accessCodeRepository.save(accessCodeEntity.get());
            updatedAccessCodeEntity.getCreator().setPassword("");
            return Optional.of(updatedAccessCodeEntity);
        }
        return Optional.empty();
    }
}
