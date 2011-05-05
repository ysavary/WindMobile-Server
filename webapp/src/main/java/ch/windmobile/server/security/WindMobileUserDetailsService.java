package ch.windmobile.server.security;

import java.util.Arrays;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ch.windmobile.server.socialmodel.ServiceLocator;
import ch.windmobile.server.socialmodel.ServiceLocator.ServiceLocatorException;
import ch.windmobile.server.socialmodel.UserService;
import ch.windmobile.server.socialmodel.UserService.UserNotFound;
import ch.windmobile.server.socialmodel.xml.User;

public class WindMobileUserDetailsService implements UserDetailsService {

    private UserService userService;

    public WindMobileUserDetailsService(ServiceLocator serviceLocator) throws ServiceLocatorException {
        serviceLocator.connect(null);
        userService = serviceLocator.getService(UserService.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        User user;
        try {
            user = userService.findByEmail(username);
            return new org.springframework.security.core.userdetails.User(user.getEmail(), null, true, true, true, true,
                Arrays.asList(WindMobileAuthenticationProvider.roleUserAuthority));
        } catch (UserNotFound e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}
