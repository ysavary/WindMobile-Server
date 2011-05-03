package ch.windmobile.server.security;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ch.windmobile.server.socialmodel.AuthenticationService;
import ch.windmobile.server.socialmodel.ServiceLocator;
import ch.windmobile.server.socialmodel.ServiceLocator.ServiceLocatorException;

public class WindMobileUserDetailsService implements UserDetailsService {
    private static final GrantedAuthority userRole = new GrantedAuthorityImpl("user");

    private ServiceLocator serviceLocator;
    private AuthenticationService authenticationService;

    public WindMobileUserDetailsService(ServiceLocator serviceLocator) throws ServiceLocatorException {
        serviceLocator.connect(null);
        authenticationService = serviceLocator.getService(AuthenticationService.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        Collection<? extends GrantedAuthority> userRoles = Arrays.asList(userRole);
        return new User("yann", "123", true, true, true, true, userRoles);
    }
}
