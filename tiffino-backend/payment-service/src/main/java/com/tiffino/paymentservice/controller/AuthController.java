package com.tiffino.paymentservice.controller;//package com.tiffino.paymentservice.controller;
//
//import com.tiffino.paymentservice.security.JwtUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Set;
//
//@RestController
//@RequestMapping("/auth")
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final JwtUtil jwtUtil;
//
//    @GetMapping("/token")
//    public String generateToken(@RequestParam String username, @RequestParam Set<String> roles) {
//        return jwtUtil.generateToken(username, roles);
//    }
//}
