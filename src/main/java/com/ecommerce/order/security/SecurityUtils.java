package com.ecommerce.order.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;

/**
 * Security utility methods.
 */
public class SecurityUtils {

    private SecurityUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Get current authenticated user ID.
     * 
     * @return User ID or null if not authenticated
     */
    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return (String) authentication.getPrincipal();
        }
        return null;
    }

    /**
     * Extract user ID from JWT token.
     * In production, use a proper JWT library.
     * 
     * @param token JWT token
     * @return User ID
     */
    public static String extractUserId(String token) {
        // Placeholder implementation
        // In production, decode JWT and extract subject claim
        return "user123";
    }

    /**
     * Extract roles from JWT token.
     * In production, use a proper JWT library.
     * 
     * @param token JWT token
     * @return List of roles
     */
    public static List<String> extractRoles(String token) {
        // Placeholder implementation
        // In production, decode JWT and extract roles claim
        return Arrays.asList("CUSTOMER", "ADMIN");
    }
}
