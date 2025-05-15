package com.quang.template.service;


import com.quang.template.config.jwt.JwtService;
import com.quang.template.dto.request.AuthRequest;
import com.quang.template.dto.request.RefreshTokenRequest;
import com.quang.template.dto.request.RegisterRequest;
import com.quang.template.dto.response.AuthResponse;
import com.quang.template.model.Enum.Role;
import com.quang.template.model.User;
import com.quang.template.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(Role.USER)
                .build();
        Optional<User> findUser = userRepository.findByUsername(user.getUsername())
                .or(() -> userRepository.findByEmail(user.getEmail()));
        if (findUser.isPresent()) {
            throw new IllegalArgumentException("User with username or email already exists");
        }
        userRepository.save(user);
        User savedUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found after saving"));
        var jwtToken = jwtService.generateToken(savedUser);
        var refreshToken = jwtService.generateRefreshToken(savedUser);
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        try {
            log.info("Authenticating user: {}", request.getUsername());
            var user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new BadCredentialsException("User not found with username: " + request.getUsername()));
            if (!user.isAccountNonLocked()) {
                log.error("Account is locked for user: {}", request.getUsername());
                throw new LockedException("User account is locked");
            }
            if (!user.isEnabled()) {
                log.error("Account is disabled for user: {}", request.getUsername());
                throw new DisabledException("User account is disabled");
            }
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            log.info("Authentication successful for user: {}", request.getUsername());
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            return AuthResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (LockedException e) {
            log.error("User account is locked: {}", request.getUsername());
            throw e;
        } catch (DisabledException e) {
            log.error("User account is disabled: {}", request.getUsername());
            throw e;
        } catch (BadCredentialsException e) {
            log.error("Bad credentials for user: {}", request.getUsername());
            throw e;
        } catch (Exception e) {
            log.error("Error during authentication: {}", e.getMessage(), e);
            throw e;
        }
    }


    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        String username = jwtService.extractUsername(refreshToken);
        if (username == null) {
            log.error("Invalid refresh token provided");
            throw new BadCredentialsException("Invalid refresh token");
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        if (!jwtService.isTokenValid(refreshToken, user)) {
            log.error("Refresh token validation failed for user: {}", username);
            throw new BadCredentialsException("Invalid refresh token");
        }

        String newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        log.info("Generated new tokens for user: {}", username);
        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
}