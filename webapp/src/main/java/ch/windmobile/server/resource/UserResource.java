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

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import ch.windmobile.server.socialmodel.ServiceLocator;
import ch.windmobile.server.socialmodel.UserService;
import ch.windmobile.server.socialmodel.xml.Favorite;
import ch.windmobile.server.socialmodel.xml.Favorites;

public class UserResource {
    private ServiceLocator serviceLocator;

    private String email;

    UriInfo uriInfo;
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
            List<Favorite> result = userService.getFavorites(email);
            Favorites favorites = new Favorites();
            favorites.getFavorites().addAll(result);
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
            Favorite favorite = new Favorite();
            favorite.setStationId(stationId);
            List<Favorite> result = userService.addToFavorites(email, Arrays.asList(favorite));
            Favorites favorites = new Favorites();
            favorites.getFavorites().addAll(result);
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
            Favorite favorite = new Favorite();
            favorite.setStationId(stationId);
            List<Favorite> result = userService.removeFromFavorites(email, Arrays.asList(favorite));
            Favorites favorites = new Favorites();
            favorites.getFavorites().addAll(result);
            return favorites;
        } catch (Exception e) {
            ExceptionHandler.treatException(e);
            return null;
        }
    }
}
