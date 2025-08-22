package com.ubbackend.services;

import com.ubbackend.DTO.UserEntityDTO;
import com.ubbackend.DTO.UserResponseDTO;
import com.ubbackend.DTO.UserUpdateDTO;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserResponseDTO> getUsers();
    Optional<UserResponseDTO> getUser(Long id);
    Optional<UserResponseDTO> createUser(UserEntityDTO userEntityDTO) throws Exception;
    Optional<UserResponseDTO> updateUser(Long id, UserUpdateDTO userUpdateDTO) throws Exception;
    boolean deleteUser(Long id);
}
