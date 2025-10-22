package com.ubbackend.serviceTest;

import com.ubbackend.DTO.UserEntityDTO;
import com.ubbackend.DTO.UserResponseDTO;
import com.ubbackend.DTO.UserUpdateDTO;
import com.ubbackend.enumeration.ERol;
import com.ubbackend.exception.ResourceNotCreatedException;
import com.ubbackend.exception.ResourceNotFoundException;
import com.ubbackend.model.AccessCodeEntity;
import com.ubbackend.model.RolEntity;
import com.ubbackend.model.UserEntity;
import com.ubbackend.repository.AccessCodeRepository;
import com.ubbackend.repository.UserRepository;
import com.ubbackend.servicesImpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccessCodeRepository accessCodeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUser_UserExists() {

        RolEntity rolEntity = new RolEntity();
        rolEntity.setId(1L);
        rolEntity.setRole(ERol.ADMIN);
        Set<RolEntity> roles = new HashSet<>();
        roles.add(rolEntity);

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setName("user");
        user.setEmail("user@example.com");
        user.setDni(123456L);
        user.setRoles(roles);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<UserResponseDTO> result = userService.getUser(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("user", result.get().getName());
        assertEquals("user@example.com", result.get().getEmail());
        assertEquals(123456L, result.get().getDni());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUsers_ReturnsList() {
        UserEntity user1 = new UserEntity();
        user1.setId(1L);
        user1.setName("user");
        user1.setEmail("user@example.com");

        UserEntity user2 = new UserEntity();
        user2.setId(2L);
        user2.setName("María");
        user2.setEmail("maria@example.com");

        List<UserEntity> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        List<UserResponseDTO> result = userService.getUsers();

        assertEquals(2, result.size());
        assertEquals("user", result.get(0).getName());
        assertEquals("María", result.get(1).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testCreateUser_Success() throws Exception {
        UserEntityDTO dto = new UserEntityDTO();
        dto.setName("Andrés");
        dto.setEmail("andres@example.com");
        dto.setPassword("123456");
        dto.setDni(123456L);
        dto.setAccessCode(123L);

        AccessCodeEntity code = new AccessCodeEntity();
        code.setCode(123L);
        code.setActive(true);
        code.setRoleType(ERol.ADMIN);

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByDni(dto.getDni())).thenReturn(Optional.empty());
        when(accessCodeRepository.findByCode(dto.getAccessCode())).thenReturn(Optional.of(code));
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<UserResponseDTO> result = userService.createUser(dto);

        assertTrue(result.isPresent());
        assertEquals("Andrés", result.get().getName());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testCreateUser_AccessCodeNotFound() {
        UserEntityDTO dto = new UserEntityDTO();
        dto.setAccessCode(123L);

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByDni(dto.getDni())).thenReturn(Optional.empty());
        when(accessCodeRepository.findByCode(123L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.createUser(dto));
    }

    @Test
    void testCreateUser_AccessCodeExpired() {
        UserEntityDTO dto = new UserEntityDTO();
        dto.setAccessCode(123L);

        AccessCodeEntity code = new AccessCodeEntity();
        code.setCode(123L);
        code.setActive(false);

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByDni(dto.getDni())).thenReturn(Optional.empty());
        when(accessCodeRepository.findByCode(123L)).thenReturn(Optional.of(code));

        assertThrows(ResourceNotCreatedException.class, () -> userService.createUser(dto));
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        UserEntity existing = new UserEntity();
        existing.setId(1L);
        existing.setName("OldName");

        UserUpdateDTO updateDTO = new UserUpdateDTO();
        updateDTO.setName("NewName");
        updateDTO.setDni(0L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<UserResponseDTO> result = userService.updateUser(1L, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("NewName", result.get().getName());
        verify(userRepository, times(1)).save(existing);
    }

    @Test
    void testUpdateUser_NotFound() throws Exception {
        UserUpdateDTO updateDTO = new UserUpdateDTO();
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<UserResponseDTO> result = userService.updateUser(999L, updateDTO);
        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);

        boolean result = userService.deleteUser(1L);

        assertTrue(result);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.existsById(999L)).thenReturn(false);

        boolean result = userService.deleteUser(999L);

        assertFalse(result);
        verify(userRepository, never()).deleteById(anyLong());
    }


}
