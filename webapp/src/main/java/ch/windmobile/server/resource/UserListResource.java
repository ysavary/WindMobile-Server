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
package ch.windmobile.server.resource;

import java.net.HttpURLConnection;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.security.core.context.SecurityContextHolder;

import ch.windmobile.server.datasourcemodel.DataSourceException;
import ch.windmobile.server.datasourcemodel.xml.Error;
import ch.windmobile.server.security.SecurityHelper;
import ch.windmobile.server.security.WindMobileAuthenticationProvider;
import ch.windmobile.server.socialmodel.ServiceLocator;

import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.spi.resource.Singleton;

@Path("/users")
@Singleton
public class UserListResource {
    @InjectParam("serviceLocator")
    private ServiceLocator serviceLocator;

    @Context
    UriInfo uriInfo;
    @Context
    Request request;

    @Path("{email}")
    public UserResource getUser(@PathParam("email") String email) {
        try {
            if (SecurityHelper.hasRole(WindMobileAuthenticationProvider.roleAdmin) == false) {
                Error error = new Error();
                error.setCode(DataSourceException.Error.UNAUTHORIZED.getCode());
                throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).entity(error).build());
            }

            return new UserResource(uriInfo, request, serviceLocator, email);
        } catch (Exception e) {
            ExceptionHandler.treatException(e);
            return null;
        }
    }

    @Path("current")
    public UserResource getCurrentUser() {
        try {
            if (SecurityHelper.hasRole(WindMobileAuthenticationProvider.roleUser) == false) {
                Error error = new Error();
                error.setCode(DataSourceException.Error.UNAUTHORIZED.getCode());
                throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).entity(error).build());
            }

            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            return new UserResource(uriInfo, request, serviceLocator, email);
        } catch (Exception e) {
            ExceptionHandler.treatException(e);
            return null;
        }
    }
}
