package com.tiffino.paymentservice.security;//package com.tiffino.paymentservice.security;
//
//import io.jsonwebtoken.Claims;
//import jakarta.servlet.*;
//import jakarta.servlet.http.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.*;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthFilter extends OncePerRequestFilter {
//
//    private final JwtUtil jwtUtil;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//
//        final String authHeader = request.getHeader("Authorization");
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//            if (jwtUtil.validateToken(token)) {
//                Claims claims = jwtUtil.extractClaims(token);
//                String username = claims.getSubject();
//                List<GrantedAuthority> authorities = ((List<?>) claims.get("roles")).stream()
//                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
//                        .map(granted -> (GrantedAuthority) granted)
//                        .collect(Collectors.toList());
//
//                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
//                        username, null, authorities);
//                SecurityContextHolder.getContext().setAuthentication(auth);
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
