package com.tiffino.userservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiffino.userservice.entity.User;
import com.tiffino.userservice.enums.Role;
import com.tiffino.userservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GoogleOAuth implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    //private final PasswordEncoder googlePasswordEncoder;
//    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = token.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        Optional<User> existingUser = userRepository.findByEmail(email);

        User user = existingUser.map(u -> {
            u.setFullName(name);
            u.setProfileImageUrl(picture);
            u.setLastLogin(LocalDateTime.now());
            return userRepository.save(u);
        }).orElseGet(() -> {
            User newUser = User.builder()
                    .email(email)
                    .fullName(name)
                    .profileImageUrl(picture)
                    .role(Role.CUSTOMER)
                    .dateJoined(LocalDateTime.now())
                    .lastLogin(LocalDateTime.now())
                    .isActive(true)
                   // .password(googlePasswordEncoder.encode(UUID.randomUUID().toString()))
                    .password(UUID.randomUUID().toString())
                    .build();
            return userRepository.save(newUser);
        });

//        String jwtToken = jwtUtil.generateToken(email);
//
//      String redirectUrl = "http://localhost:4200/oauth2/success?token=" + jwtToken;
//       response.sendRedirect(redirectUrl);

        String jwtToken = jwtUtil.generateToken(email);

        //new change 2-9-2025
// Detect the frontend origin dynamically
        String origin = request.getHeader("Origin");
        if (origin == null)
        {
            origin = "http://localhost:4200"; // fallback for local dev
        }

        String redirectUrl = origin + "/oauth2/success?token=" + jwtToken;
        response.sendRedirect(redirectUrl);

        //      response.setContentType("text/plain");
  //     response.getWriter().write("JWT Token: " + jwtToken);

        // Send JSON response instead of plain text
       // Map<String, Object> responseBody = Map.of(
               // "token", jwtToken,
               // "email", user.getEmail(),
                //"name", user.getFullName(),
                //"role", user.getRole()
       // );

        //response.setContentType("application/json");
        //new ObjectMapper().writeValue(response.getWriter(), responseBody);

    }

}
