package com.ufcg.adptare.controller;

import com.ufcg.adptare.dto.auth.AuthenticationDTO;
import com.ufcg.adptare.dto.user.LoginDTO;
import com.ufcg.adptare.dto.auth.RegisterDTO;
import com.ufcg.adptare.infra.security.SecurityService;
import com.ufcg.adptare.model.User;
import com.ufcg.adptare.service.AuthorizationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    AuthorizationService authorizationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO userData) {
        try {
            var userNamePassword = new UsernamePasswordAuthenticationToken(userData.login(), userData.password());
            var auth = this.authenticationManager.authenticate(userNamePassword);
            var token = securityService.generateToken((User) auth.getPrincipal());
            return ResponseEntity.ok(new LoginDTO(token));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Falha na autenticação: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO userData) {
        try {
            String registrationMessage = this.authorizationService.register(userData.login(), userData.password(), userData.firstName(), userData.lastName(), userData.role());
            return ResponseEntity.ok(registrationMessage);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Falha no registro: " + e.getMessage());
        }
    }
}