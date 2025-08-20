package com.ubbackend.controller;

import com.ubbackend.DTO.UserEntityDTO;
import com.ubbackend.DTO.UserResponseDTO;
import com.ubbackend.DTO.UserUpdateDTO;
import com.ubbackend.services.UserService;
import com.ubbackend.servicesImpl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class UserController {

    private final UserService userService;
    private final UserServiceImpl userServiceImpl;

    public UserController(UserService userService, UserServiceImpl userServiceImpl) {
        this.userService = userService;
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers());
    }

    @GetMapping("/users/{email}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable String email) throws Exception {
        Optional<UserResponseDTO> userExisting = userService.getUser(email);

        return userExisting
                .map(userResponseDTO -> ResponseEntity.status(HttpStatus.OK).body(userResponseDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/create/user")
    public ResponseEntity<?> createUser(@RequestBody UserEntityDTO userEntityDTO) throws Exception {
        if(userService.createUser(userEntityDTO)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("User has been create");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO userUpdateDTO) throws Exception {
        Optional<UserResponseDTO> userUpdateExisting = userService.updateUser(id, userUpdateDTO);

        return userUpdateExisting
                .map(userUpdate -> ResponseEntity.status(HttpStatus.OK).body(userUpdate))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {

        if(userServiceImpl.deleteUser(id)) {
            return ResponseEntity.status(HttpStatus.OK).body("User successfully deleted");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
