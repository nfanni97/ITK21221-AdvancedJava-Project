package com.f197a4.registry.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.f197a4.registry.domain.security.ERole;
import com.f197a4.registry.domain.security.Role;
import com.f197a4.registry.domain.security.User;
import com.f197a4.registry.payload.request.LoginRequest;
import com.f197a4.registry.payload.response.JwtResponse;
import com.f197a4.registry.payload.response.MessageResponse;
import com.f197a4.registry.repository.RoleRepo;
import com.f197a4.registry.repository.UserRepo;
import com.f197a4.registry.security.jwt.JwtUtils;
import com.f197a4.registry.service.UserDetailsImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//register and login
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepo userRepo;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("Signing in user with name {}.",loginRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        logger.debug("Generated token: {}",jwt);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority()).collect(Collectors.toList());
        logger.info("User {} got their token.",loginRequest.getUsername());
        return ResponseEntity.ok(new JwtResponse(
            jwt,
            userDetails.getId(),
            userDetails.getUsername(),
            roles
        ));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody LoginRequest signupRequest) {
        logger.info("Signing up new user with name {}.",signupRequest.getUsername());
        if(userRepo.existsByUsername(signupRequest.getUsername())) {
            logger.info("The name {} is already taken.",signupRequest.getUsername());
            return ResponseEntity.badRequest().body(new MessageResponse("Error: username is already taken"));
        }
        User user = new User(signupRequest.getUsername(),encoder.encode(signupRequest.getPassword()));
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepo.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: user role is not found"));
        roles.add(userRole);
        user.setRoles(roles);
        userRepo.save(user);
        logger.info("User {} signed up.",signupRequest.getUsername());
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    
}
