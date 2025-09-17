package uz.darvoza.baxrin_darvoza.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.darvoza.baxrin_darvoza.confige.CustomUserDetails;
import uz.darvoza.baxrin_darvoza.enums.Roles;

public class SpringSecurityUtil {
    public static CustomUserDetails getCurrentEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        return user;
    }

    public static Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        return user.getId();
    }

    public static boolean hasRole(Roles requiredRole) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(sga ->sga.getAuthority().equals(requiredRole.name()));
    }
}
