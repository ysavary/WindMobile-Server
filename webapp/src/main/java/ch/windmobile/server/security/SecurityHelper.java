package ch.windmobile.server.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityHelper {

    public static boolean hasRole(String role) {
        boolean result = false;
        for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            String userRole = authority.getAuthority();
            if (userRole.equals(role)) {
                result = true;
                break;
            }
        }
        return result;
    }
}
