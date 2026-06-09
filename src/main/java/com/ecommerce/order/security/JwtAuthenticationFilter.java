package com.ecommerce.order.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT authentication filter.
 * Maps from legacy container-managed authentication to JWT-based auth.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) throws ServletException, IOException {
        
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            String token = authHeader.substring(BEARER_PREFIX.length());
            
            try {
                // Validate JWT token and extract claims
                // In production, use a proper JWT library (e.g., jjwt)
                String userId = SecurityUtils.extractUserId(token);
                List<String> roles = SecurityUtils.extractRoles(token);
                
                if (userId != null && !roles.isEmpty()) {
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());
                    
                    UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(userId, null, authorities);
                    
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    
                    logger.debug("Authenticated user: {} with roles: {}", userId, roles);
                }
            } catch (Exception e) {
                logger.error("JWT authentication failed: {}", e.getMessage());
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
