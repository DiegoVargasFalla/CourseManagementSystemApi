package com.ubbackend.controller;

import com.ubbackend.DTO.*;
import com.ubbackend.services.UserService;
import com.ubbackend.servicesImpl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(
        name = "User controller",
        description = "User controller where all the endpoints are located"
)
@RestController
@RequestMapping("/v1")
public class UserController {

    private final UserService userService;
    private final UserServiceImpl userServiceImpl;

    public UserController(UserService userService, UserServiceImpl userServiceImpl) {
        this.userService = userService;
        this.userServiceImpl = userServiceImpl;
    }

    @Operation(
            summary = "fetch all User",
            description = "This method return all users in DB",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of users",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            type = "array",
                                            implementation = UserResponseDTO.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ErrorResponse.class
                                    )
                            )
                    )
            }
    )
    @GetMapping("/users")

    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers());
    }
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(
            summary = "fetch a user",
            description = "Method to get a user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "A user",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = UserResponseDTO.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Student not found",
                            content =  @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> getUser(
            @Parameter(
                    name = "id",
                    description = "The user id",
                    required = true,
                    schema = @Schema(
                            type = "Integer",
                            format = "Long",
                            description = "Param ID user that needs to be fetched",
                            allowableValues = {"1", "2", "3"}
                    )
            )
            @PathVariable Long id) throws Exception {
        Optional<UserResponseDTO> userExisting = userService.getUser(id);

        return userExisting
                .map(userResponseDTO -> ResponseEntity.status(HttpStatus.OK).body(userResponseDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(
            summary = "Create a user",
            description = "Method to create user, view all attributes in UserEntityDTO",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "this method receive a class with all attributes for create user, view all attributes in UserEntityDTO",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserEntityDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "The user successfully created, and the response is the course where student was saved",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "User already exist, this verification is with user dni and user email," +
                                    "if one already exist you can´t create user",
                            content =  @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Access code has expired or not exist",
                            content =  @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PostMapping("/users/create")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserEntityDTO userEntityDTO) throws Exception {
        Optional<UserResponseDTO> userResponseDTOExists = userService.createUser(userEntityDTO);
        return userResponseDTOExists
                .map(userResponseDTO -> ResponseEntity.status(HttpStatus.OK).body(userResponseDTO))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @Operation(
            summary = "Update user",
            description = "Method to update user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = """
                            this method receive a class with all attributes to update student.
                            \s
                             IMPORTANT\s
                            \s
                            if you don't want to update any attribute of type text, just send (null).
                            \s
                            In the case of Dni, if you don't want to update send 0, Otherwise send new dni.
                            \s""",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserUpdateDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "The user successfully update",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = StudentRecursionDTO.class,
                                            description = "The user updated"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "The user doesn't exist",
                            content =  @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PatchMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(
                    name = "id",
                    description = "The user id",
                    required = true,
                    schema = @Schema(
                            type = "Integer",
                            description = "Param ID user that needs to be updated",
                            allowableValues = {"1", "2", "3"}
                    )
            )
            @PathVariable Long id, @RequestBody UserUpdateDTO userUpdateDTO) throws Exception {
        Optional<UserResponseDTO> userUpdateExisting = userService.updateUser(id, userUpdateDTO);

        return userUpdateExisting
                .map(userUpdate -> ResponseEntity.status(HttpStatus.OK).body(userUpdate))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(
            summary = "Delete user",
            description = "Method to remove a user whit the id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "user successfully deleted",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = String.class,
                                            description = "Info message that user was deleted"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "The user id doesn't exist",
                            content =  @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(
            @Parameter(
                    name = "id",
                    description = "The user id",
                    required = true,
                    schema = @Schema(
                            type = "Integer",
                            description = "Param ID user that needs to be removed",
                            allowableValues = {"1", "2", "3"}
                    )
            )
            @PathVariable Long id) {

        if(userServiceImpl.deleteUser(id)) {
            return ResponseEntity.status(HttpStatus.OK).body("User successfully deleted");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
