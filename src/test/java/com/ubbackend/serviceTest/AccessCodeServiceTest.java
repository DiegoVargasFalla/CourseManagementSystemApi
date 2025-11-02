package com.ubbackend.serviceTest;

import com.ubbackend.DTO.AccessCodeCreatedDTO;
import com.ubbackend.enumeration.ERol;
import com.ubbackend.model.AccessCodeEntity;
import com.ubbackend.model.UserEntity;
import com.ubbackend.repository.AccessCodeRepository;
import com.ubbackend.repository.UserRepository;
import com.ubbackend.servicesImpl.AccessCodeServiceImpl;
import com.ubbackend.servicesImpl.MailSenderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class AccessCodeServiceTest {


    @Mock
    private AccessCodeRepository accessCodeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MailSenderImpl mailSender;

    @InjectMocks
    private AccessCodeServiceImpl accessCodeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ------------------------------------------------------------
    // getAllAccessCode()
    // ------------------------------------------------------------
    @Test
    void getAllAccessCode_ShouldReturnAccessCodesWithEmptyPassword() {
        UserEntity creator = new UserEntity();
        creator.setPassword("12345");

        AccessCodeEntity entity = new AccessCodeEntity();
        entity.setCreator(creator);

        when(accessCodeRepository.findAll()).thenReturn(List.of(entity));

        List<AccessCodeEntity> result = accessCodeService.getAllAccessCode();

        assertEquals(1, result.size());
        assertEquals("", result.get(0).getCreator().getPassword());
        verify(accessCodeRepository).findAll();
    }

    // ------------------------------------------------------------
    // generateAccessCode()
    // ------------------------------------------------------------
    @Test
    void generateAccessCode_ShouldCreateAndSendMail_WhenUserExists() throws Exception {
        AccessCodeCreatedDTO dto = new AccessCodeCreatedDTO();
        dto.setEmailCreator("creator@example.com");
        dto.setEmailRecipient("recipient@example.com");
        dto.setRolType(ERol.ADMIN.toString());

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("creator@example.com");

        when(userRepository.findByEmail(dto.getEmailCreator())).thenReturn(Optional.of(userEntity));

        AccessCodeEntity savedEntity = new AccessCodeEntity();
        savedEntity.setCode(123456L);
        savedEntity.setCreator(userEntity);
        savedEntity.setEmailRecipient("recipient@example.com");
        savedEntity.setRoleType(ERol.ADMIN);

        when(accessCodeRepository.save(any(AccessCodeEntity.class))).thenReturn(savedEntity);

        Optional<String> result = accessCodeService.generateAccessCode(dto);

        assertTrue(result.isPresent());
        assertEquals("AccessCode successfully created", result.get());
        verify(accessCodeRepository).save(any(AccessCodeEntity.class));
        verify(mailSender).sendMail(
                eq(dto.getEmailRecipient()),
                eq("Codigo de registro"),
                anyString(),
                eq("https://iviva06.github.io/PaginaWeb/#/register"),
                eq(StandardCharsets.UTF_8)
        );
    }

    @Test
    void generateAccessCode_ShouldReturnEmpty_WhenUserNotFound() throws Exception {
        AccessCodeCreatedDTO dto = new AccessCodeCreatedDTO();
        dto.setEmailCreator("unknown@example.com");

        when(userRepository.findByEmail(dto.getEmailCreator())).thenReturn(Optional.empty());

        Optional<String> result = accessCodeService.generateAccessCode(dto);

        assertTrue(result.isEmpty());
        verify(accessCodeRepository, never()).save(any());
        verify(mailSender, never()).sendMail(any(), any(), any(), any(), any());
    }

    // ------------------------------------------------------------
    // cancelAccessCode()
    // ------------------------------------------------------------
    @Test
    void cancelAccessCode_ShouldDeactivateAccessCode_WhenFound() {
        AccessCodeEntity entity = new AccessCodeEntity();
        entity.setActive(true);
        entity.setCode(999999L);
        entity.setCreator(new UserEntity());
        entity.getCreator().setPassword("secret");

        when(accessCodeRepository.findByCode(999999L)).thenReturn(Optional.of(entity));
        when(accessCodeRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Optional<AccessCodeEntity> result = accessCodeService.cancelAccessCode(999999L);

        assertTrue(result.isPresent());
        assertFalse(result.get().getActive());
        assertEquals("", result.get().getCreator().getPassword());
        verify(accessCodeRepository).findByCode(999999L);
        verify(accessCodeRepository).save(any(AccessCodeEntity.class));
    }

    @Test
    void cancelAccessCode_ShouldReturnEmpty_WhenNotFound() {
        when(accessCodeRepository.findByCode(111111L)).thenReturn(Optional.empty());

        Optional<AccessCodeEntity> result = accessCodeService.cancelAccessCode(111111L);

        assertTrue(result.isEmpty());
        verify(accessCodeRepository).findByCode(111111L);
        verify(accessCodeRepository, never()).save(any());
    }

    // ------------------------------------------------------------
    // checkAccessCode()
    // ------------------------------------------------------------
    @Test
    void checkAccessCode_ShouldReturnTrue_WhenExists() throws Exception {
        when(accessCodeRepository.findByCode(123456L)).thenReturn(Optional.of(new AccessCodeEntity()));

        boolean exists = accessCodeService.checkAccessCode(123456L);

        assertTrue(exists);
        verify(accessCodeRepository).findByCode(123456L);
    }

    @Test
    void checkAccessCode_ShouldReturnFalse_WhenNotExists() throws Exception {
        when(accessCodeRepository.findByCode(654321L)).thenReturn(Optional.empty());

        boolean exists = accessCodeService.checkAccessCode(654321L);

        assertFalse(exists);
        verify(accessCodeRepository).findByCode(654321L);
    }
}
