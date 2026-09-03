package com.jssv.globalinvoice.service;

import com.jssv.globalinvoice.dto.AuthResponseDTO;
import com.jssv.globalinvoice.dto.LoginRequestDTO;
import com.jssv.globalinvoice.entity.Role;
import com.jssv.globalinvoice.entity.User;
import com.jssv.globalinvoice.exception.NoDataFoundException;
import com.jssv.globalinvoice.repository.RoleRepository;
import com.jssv.globalinvoice.repository.UserRepository;
import com.jssv.globalinvoice.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository usuarioRepository;

    @Mock
    private RoleRepository rolRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_shouldAuthenticateUserAndReturnToken() {
        LoginRequestDTO request = LoginRequestDTO.builder()
                .email("admin@test.com")
                .password("123456")
                .build();

        Role role = new Role();
        role.setId(1);
        role.setNombre("AUDITOR");

        User user = User.builder()
                .id(10)
                .email("admin@test.com")
                .password("encodedPassword")
                .active(true)
                .roles(Set.of(role))
                .build();

        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(usuarioRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        AuthResponseDTO response = authService.login(request);

        assertNotNull(response);
        assertEquals(10, response.getId());
        assertEquals("admin@test.com", response.getEmail());
        assertEquals("jwt-token", response.getToken());
        assertTrue(response.getRoles().contains("ROLE_AUDITOR"));

        verify(authenticationManager).authenticate(any());
        verify(usuarioRepository).findByEmail("admin@test.com");
        verify(jwtService).generateToken(user);
    }

    @Test
    void login_shouldThrowWhenUserDoesNotExist() {
        LoginRequestDTO request = LoginRequestDTO.builder()
                .email("missing@test.com")
                .password("123456")
                .build();

        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(usuarioRepository.findByEmail("missing@test.com")).thenReturn(Optional.empty());

        NoDataFoundException exception = assertThrows(
                NoDataFoundException.class,
                () -> authService.login(request)
        );

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(authenticationManager).authenticate(any());
        verify(usuarioRepository).findByEmail("missing@test.com");
        verify(jwtService, never()).generateToken(any());
    }
}
