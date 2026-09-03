package com.jssv.globalinvoice.service;

import com.jssv.globalinvoice.dto.AuthResponseDTO;
import com.jssv.globalinvoice.dto.LoginRequestDTO;
import com.jssv.globalinvoice.entity.Role;
import com.jssv.globalinvoice.entity.User;
import com.jssv.globalinvoice.exception.NoDataFoundException;
import com.jssv.globalinvoice.repository.RoleRepository;
import com.jssv.globalinvoice.repository.UserRepository;
import com.jssv.globalinvoice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository usuarioRepository;
    private final RoleRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NoDataFoundException("Usuario no encontrado"));

        String jwtToken = jwtService.generateToken(usuario);

        return AuthResponseDTO.builder()
                .id(usuario.getId())
                .email(usuario.getEmail())
                .token(jwtToken)
                .roles(usuario.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();
    }

}
