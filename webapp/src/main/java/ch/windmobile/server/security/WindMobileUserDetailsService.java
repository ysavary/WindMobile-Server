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
