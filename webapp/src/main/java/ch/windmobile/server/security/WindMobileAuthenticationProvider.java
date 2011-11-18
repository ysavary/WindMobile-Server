/*******************************************************************************
 * Copyright (c) 2011 epyx SA.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License along
 * with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package ch.windmobile.server.security;

import java.util.Arrays;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import ch.windmobile.server.socialmodel.AuthenticationService;
import ch.windmobile.server.socialmodel.ServiceLocator;
import ch.windmobile.server.socialmodel.ServiceLocator.ServiceLocatorException;

public class WindMobileAuthenticationProvider implements AuthenticationProvider {
    public static final String roleAnonymous = "ROLE_ANONYMOUS";
    public static final String roleUser = "ROLE_USER";
    public static final String roleAdmin = "ROLE_ADMIN";
    public static final GrantedAuthority roleAnonymousAuthority = new SimpleGrantedAuthority(roleAnonymous);
    public static final GrantedAuthority roleUserAuthority = new SimpleGrantedAuthority(roleUser);
    public static final GrantedAuthority roleAdminAuthority = new SimpleGrantedAuthority(roleAdmin);

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
            String currentRole = authenticationService.authenticate((String) token.getPrincipal(), (String) token.getCredentials());
            if (roleAdmin.equals(currentRole)) {
                return new UsernamePasswordAuthenticationToken(token.getPrincipal(), token.getCredentials(), Arrays.asList(roleAdminAuthority));
            } else {
                return new UsernamePasswordAuthenticationToken(token.getPrincipal(), token.getCredentials(), Arrays.asList(roleUserAuthority));
            }
        } catch (Exception e) {
            // Silently return ROLE_ANONYMOUS instead throwing an HTTP exception (401: Unauthorized) which will be
            // intercepted by the container
            Authentication anonymousToken = new UsernamePasswordAuthenticationToken(token.getPrincipal(), token.getCredentials(),
                Arrays.asList(roleAnonymousAuthority));
            anonymousToken.setAuthenticated(false);
            return anonymousToken;
        }
    }

    @Override
    public boolean supports(Class<? extends Object> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
