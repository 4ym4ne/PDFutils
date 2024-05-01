package com.pdfutils.services;

import com.pdfutils.domain.Role;
import com.pdfutils.domain.DTO.login.LoginRequest;
import com.pdfutils.domain.DTO.login.LoginResponse;
import com.pdfutils.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${jwt.issuer:pdf_files}")
    private String issuer;

    @Value("${jwt.expiration-sec:300}")
    private Long expirationSeconds;

    public AuthService(JwtEncoder jwtEncoder, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse authenticate(LoginRequest loginRequest) {
        return userRepository.findByUsername(loginRequest.username())
                .filter(user -> user.check(loginRequest, passwordEncoder))
                .map(user -> {
                    var now = Instant.now();
                    var claims = JwtClaimsSet.builder()
                            .issuer(issuer)
                            .subject(user.getId().toString())
                            .issuedAt(now)
                            .expiresAt(now.plusSeconds(expirationSeconds))
                            .claim("scope", user.getRoleSet().stream()
                                    .map(Role::getRoleName)
                                    .collect(Collectors.joining(" ")).toUpperCase())
                            .build();
                    return new LoginResponse(
                            jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue(),
                            expirationSeconds);
                })
                .orElseThrow(() -> new BadCredentialsException("Invalid login Info!"));
    }
}
