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
