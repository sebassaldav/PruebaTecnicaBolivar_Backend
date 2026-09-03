package com.jssv.globalinvoice.controller;


import com.jssv.globalinvoice.dto.AuthResponseDTO;
import com.jssv.globalinvoice.dto.LoginRequestDTO;
import com.jssv.globalinvoice.dto.WrapperResponse;
import com.jssv.globalinvoice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<WrapperResponse<AuthResponseDTO>> login(@RequestBody LoginRequestDTO request) {
        return new WrapperResponse<>(authService.login(request), true, "success").createResponse(HttpStatus.OK);
    }
}
