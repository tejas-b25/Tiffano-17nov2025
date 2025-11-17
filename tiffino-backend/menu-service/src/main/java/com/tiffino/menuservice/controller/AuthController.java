//package com.tiffino.menuservice.controller;
//
//import com.tiffino.menu.dto.LoginRequest;
//import com.tiffino.menu.security.CustomUserDetailsService;
//import com.tiffino.menu.security.JwtService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final AuthenticationManager authManager;
//    private final JwtService jwtService;
//    private final CustomUserDetailsService userDetailsService;
//
//    @PostMapping("/login")
//    public String login(@RequestBody LoginRequest request) {
//        authManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
//        );
//        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
//        return jwtService.generateToken(user.getUsername());
//    }
//}
