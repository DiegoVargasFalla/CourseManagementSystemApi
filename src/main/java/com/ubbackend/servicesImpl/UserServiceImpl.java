package com.ubbackend.servicesImpl;

import com.ubbackend.DTO.UserEntityDTO;
import com.ubbackend.DTO.UserResponseDTO;
import com.ubbackend.DTO.UserUpdateDTO;
import com.ubbackend.exception.ResourceNotCreatedException;
import com.ubbackend.exception.ResourceNotFoundException;
import com.ubbackend.enumeration.ERol;
import com.ubbackend.model.AccessCodeEntity;
import com.ubbackend.model.RolEntity;
import com.ubbackend.model.UserEntity;
import com.ubbackend.repository.AccessCodeRepository;
import com.ubbackend.repository.UserRepository;
import com.ubbackend.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    /**
     * este metodo retorna la lista de usuario completa
     * @return lista de userResponseList
     */
    @Override
    public List<UserResponseDTO> getUsers() {

        List<UserEntity> usersResponseDTO = userRepository.findAll();
        List<UserResponseDTO> userResponseDTOList = new ArrayList<>();

        for(UserEntity user : usersResponseDTO) {
            userResponseDTOList.add(extractUser(user));
        }
        return userResponseDTOList;
    }

    /**
     * metodo para buscar un usuario por email
     * @param id de usuario
     * @return tipo de dato Optionl con UserResponseDTO dentro
     */
    @Override
    public Optional<UserResponseDTO> getUser(Long id) {
        Optional<UserEntity> userExisting = userRepository.findById(id);

        if(userExisting.isPresent()) {
            UserEntity user = userExisting.get();
            return Optional.of(extractUser(user));
        }
        return Optional.empty();
    }

    /**
     * metodo para crear un usuario
     * @param userEntityDTO con los atributos necearios para crear el usuario
     * @return boolean para verificar si se creo o no
     * @throws Exception si el usuario ya existe informa con una excepcion, la verificaicon se hace con el email
     */
    @Override
    @Transactional
    public Optional<UserResponseDTO> createUser(UserEntityDTO userEntityDTO) throws Exception {

        Optional<UserEntity> optionalUserEntityEmail = userRepository.findByEmail(userEntityDTO.getEmail());
        Optional<UserEntity> optionalUserEntityDni = userRepository.findByDni(userEntityDTO.getDni());

        if(optionalUserEntityEmail.isEmpty() && optionalUserEntityDni.isEmpty()) {

            Optional<AccessCodeEntity> accessCodeExisting = accessCodeRepository.findByCode(userEntityDTO.getAccessCode());

            if(accessCodeExisting.isPresent() ) {

                AccessCodeEntity accessCodeEntity = accessCodeExisting.get();

                if(accessCodeEntity.getActive()) {

                    accessCodeEntity.setActive(false);

                    RolEntity rolEntity = new RolEntity();

                    if(accessCodeEntity.getRoleType().toString().equals(ERol.SUPER_ADMIN.toString())) {
                        rolEntity.setRole(ERol.SUPER_ADMIN);
                    } else if(accessCodeEntity.getRoleType().toString().equals(ERol.ADMIN.toString())) {
                        rolEntity.setRole(ERol.ADMIN);
                    }

                    Set<RolEntity> roles = new HashSet<>();
                    roles.add(rolEntity);

                    UserEntity userEntity = new UserEntity();
                    userEntity.setName(userEntityDTO.getName());
                    userEntity.setEmail(userEntityDTO.getEmail());
                    userEntity.setPassword(passwordEncoder.encode(userEntityDTO.getPassword()));
                    userEntity.setDni(userEntityDTO.getDni());
                    userEntity.setRoles(roles);

                    UserEntity user = userRepository.save(userEntity);
                    return Optional.of(extractUser(user));
                }
                else {
                    throw new ResourceNotCreatedException("Access code has expired");
                }
            } else {
                throw new ResourceNotFoundException("Access code not found");
            }
        }
        return Optional.empty();
    }

    /**
     * metodo para actualizar un usuario
     * @param id para buscar el usuario
     * @param userUpdateDTO DTO con los atributos nuevos a cambiar en un usuario
     * @return Optional con un UserResponse dentro que tiene los atributos actualizados y si no un empty (sin nada)
     * @throws Exception si el usuario no existe informa con un Excepción
     */
    @Override
    public Optional<UserResponseDTO> updateUser(Long id, UserUpdateDTO userUpdateDTO) throws Exception {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);

        if(optionalUserEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();

            if(userUpdateDTO.getName() != null) {
                userEntity.setName(userUpdateDTO.getName());
            }
            if(userUpdateDTO.getEmail() != null) {
                userEntity.setEmail(userUpdateDTO.getEmail());
            }
            if(userUpdateDTO.getDni() != 0) {
                userEntity.setDni(userUpdateDTO.getDni());
            }

            UserEntity userUpdate = userRepository.save(userEntity);

            return Optional.of(extractUser(userUpdate));
        }
        return Optional.empty();
    }

    /**
     * metodo para eliminar un usuario
     * @param id del usuario a eliminar
     * @return valor booleano para verificar que se elimino y si no existe retorna false
     */
    @Override
    public boolean deleteUser(Long id) {

        if(userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public static UserResponseDTO extractUser(UserEntity user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();

        userResponseDTO.setId(user.getId());
        userResponseDTO.setName(user.getName());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setDni(user.getDni());
        return userResponseDTO;
    }
}
