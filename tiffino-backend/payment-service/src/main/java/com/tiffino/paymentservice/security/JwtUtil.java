package com.tiffino.paymentservice.security;//package com.tiffino.paymentservice.security;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.Date;
//import java.util.Set;
//
//@Component
//public class JwtUtil {
//
//    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//    private final long jwtExpirationMs = 86400000; // 1 day
//
//    public String generateToken(String username, Set<String> roles) {
//        return Jwts.builder()
//                .setSubject(username)
//                .claim("roles", roles)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
//                .signWith(key)
//                .compact();
//    }
//
//    public Claims extractClaims(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            extractClaims(token);
//            return true;
//        } catch (JwtException e) {
//            return false;
//        }
//    }
//}
