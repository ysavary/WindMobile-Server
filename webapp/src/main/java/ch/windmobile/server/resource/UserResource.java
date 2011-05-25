package ch.windmobile.server.resource;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import ch.windmobile.server.socialmodel.ServiceLocator;
import ch.windmobile.server.socialmodel.UserService;
import ch.windmobile.server.socialmodel.xml.Favorites;

public class UserResource {
    private ServiceLocator serviceLocator;

    private String email;

    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    String stationId;

    public UserResource(UriInfo uriInfo, Request request, ServiceLocator serviceLocator, String email) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.serviceLocator = serviceLocator;
        this.email = email;
    }

    @GET
    @Path("favorites")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Favorites getFavorites() {
        try {
            UserService userService = serviceLocator.getService(UserService.class);
            List<String> result = userService.getFavorites(email);
            Favorites favorites = new Favorites();
            favorites.getStationIds().addAll(result);
            return favorites;
        } catch (Exception e) {
            ExceptionHandler.treatException(e);
            return null;
        }
    }

    @DELETE
    @Path("favorites")
    public void clearFavorite() {
        try {
            UserService userService = serviceLocator.getService(UserService.class);
            userService.clearFavorites(email);
        } catch (Exception e) {
            ExceptionHandler.treatException(e);
        }
    }

    @PUT
    @Path("favorites/{stationId}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Favorites addToFavorite(@PathParam("stationId") String stationId) {
        try {
            UserService userService = serviceLocator.getService(UserService.class);
            List<String> result = userService.addToFavorites(email, Arrays.asList(stationId));
            Favorites favorites = new Favorites();
            favorites.getStationIds().addAll(result);
            return favorites;
        } catch (Exception e) {
            ExceptionHandler.treatException(e);
            return null;
        }
    }

    @DELETE
    @Path("favorites/{stationId}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Favorites removeFromFavorite(@PathParam("stationId") String stationId) {
        try {
            UserService userService = serviceLocator.getService(UserService.class);
            List<String> result = userService.removeFromFavorites(email, Arrays.asList(stationId));
            Favorites favorites = new Favorites();
            favorites.getStationIds().addAll(result);
            return favorites;
        } catch (Exception e) {
            ExceptionHandler.treatException(e);
            return null;
        }
    }
}
