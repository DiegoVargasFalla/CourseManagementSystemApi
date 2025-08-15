package com.ubbackend.services;

import com.ubbackend.DTOs.UserEntityDTO;

public interface UserService {
    boolean createUser(UserEntityDTO userEntityDTO) throws Exception;
}
