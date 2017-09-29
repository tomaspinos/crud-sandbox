package org.jaweze.hello.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {
        throw new UnsupportedOperationException();
    }

    public static boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    public static boolean hasRole(CustomRoles role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && role.getMatchingRoles().stream()
                .anyMatch(r -> authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + r.name())));
    }

}
