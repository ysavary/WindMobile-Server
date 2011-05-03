package ch.windmobile.server.security;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

import ch.windmobile.server.socialmodel.AuthenticationService;
import ch.windmobile.server.socialmodel.ServiceLocator;
import ch.windmobile.server.socialmodel.ServiceLocator.ServiceLocatorException;

public class WindMobileAuthenticationProvider implements AuthenticationProvider {
    private static final GrantedAuthority userRole = new GrantedAuthorityImpl("user");

    private AuthenticationService authenticationService;

    public WindMobileAuthenticationProvider(ServiceLocator serviceLocator) throws ServiceLocatorException {
        authenticationService = serviceLocator.connect(null).getService(AuthenticationService.class);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof UsernamePasswordAuthenticationToken == false) {
            throw new ProviderNotFoundException("Only UsernamePasswordAuthenticationToken is supported");
        }

        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        try {
            authenticationService.authenticate((String) token.getPrincipal(), (String) token.getCredentials());
            Collection<? extends GrantedAuthority> userRoles = Arrays.asList(userRole);
            UsernamePasswordAuthenticationToken successToken = new UsernamePasswordAuthenticationToken(token.getPrincipal(), token.getCredentials(),
                userRoles);
            return successToken;
        } catch (Exception e) {
            throw new BadCredentialsException("Bad credentials", e);
        }
    }

    @Override
    public boolean supports(Class<? extends Object> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
