package com.ubbackend.controller;

import com.ubbackend.DTOs.CreateAccessCodeDTO;
import com.ubbackend.DTOs.UserEntityDTO;
import com.ubbackend.services.AccessCodeService;
import com.ubbackend.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create/user")
    public ResponseEntity<?> createUser(@RequestBody UserEntityDTO userEntityDTO) throws Exception {
        if(userService.createUser(userEntityDTO)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("User has been create");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
