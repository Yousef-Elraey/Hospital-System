package com.hospital.auth.service;

import com.hospital.auth.dto.request.LoginRequest;
import com.hospital.auth.dto.response.LoginResponse;
import com.hospital.common.exception.HospitalBusinessException;
import com.hospital.common.security.AuthenticatedUser;
import com.hospital.common.security.CurrentUser;
import com.hospital.common.security.JwtService;
import com.hospital.entity.Status;
import com.hospital.entity.User;
import com.hospital.redis.service.TokenBlackListService;
import com.hospital.user.dto.request.CreateUserRequest;
import com.hospital.user.dto.response.CreateUserResponse;
import com.hospital.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenBlackListService tokenBlackListService;

    public CreateUserResponse register(CreateUserRequest userRequest) {
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new HospitalBusinessException("user already exist");
        }

        User userDb = new User();
        userDb.setUserName(userRequest.getUserName())
                .setPhone(userRequest.getPhone())
                .setEmail(userRequest.getEmail())
                .setPassword(userRequest.getPassword())
                .setFullName(userRequest.getFullName())
                .setAddress(userRequest.getAddress())
                .setSummary(userRequest.getSummary())
                .setRole(userRequest.getRole())
                .setActive(true)
                .setStatus(Status.ACTIVE);

        userDb.setPassword(passwordEncoder.encode(userDb.getPassword()));
        userRepository.save(userDb);

        CreateUserResponse userResponse = new CreateUserResponse();
        userResponse.setId(userDb.getId());
        return userResponse;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() ->
                new HospitalBusinessException("user with email " + loginRequest.getEmail() + " not found"));
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(user);
            Date expiresIn = jwtService.extractExpiration(token);

            LoginResponse response = new LoginResponse();
            response.setToken(token)
                    .setExpiresIn(expiresIn);
            user.setActive(Boolean.TRUE);
            userRepository.save(user);
            return response;
        }
        throw new HospitalBusinessException("you are not authenticated");
    }

    public void logout(HttpServletRequest request) {
        AuthenticatedUser currentUser = CurrentUser.getCurrentUser();

        Optional<User> userOp = userRepository.findByEmail(currentUser.getEmail());
        if (userOp.isEmpty()) {
            throw new HospitalBusinessException("user not found");
        }
        User user = userOp.get();
        String authHeader =
                request.getHeader("Authorization");

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {
            throw new HospitalBusinessException("Token not found");
        }

        String token = authHeader.substring(7);
        Date expiration = jwtService.extractExpiration(token);
        long ttl = expiration.getTime() - System.currentTimeMillis();
        if (ttl > 0) {
            tokenBlackListService
                    .blacklistToken(
                            token,
                            ttl
                    );
        }
        user.setActive(Boolean.FALSE);
        userRepository.save(user);
    }

}
