package com.ubbackend.services;

import com.ubbackend.DTO.UserEntityDTO;

public interface UserService {
    boolean createUser(UserEntityDTO userEntityDTO) throws Exception;
}
