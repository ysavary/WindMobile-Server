package ch.windmobile.server.resource;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import ch.windmobile.server.socialmodel.ServiceLocator;

import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.spi.resource.Singleton;

@Path("/chatrooms")
@Singleton
public class ChatRoomListResource {
    @InjectParam("serviceLocator")
    private ServiceLocator serviceLocator;

    @Context
    UriInfo uriInfo;
    @Context
    Request request;

    @Path("{chatRoomId}")
    public ChatRoomResource getMain(@PathParam("chatRoomId") String chatRoomId) {
        try {
            return new ChatRoomResource(uriInfo, request, serviceLocator, chatRoomId);
        } catch (Exception e) {
            ExceptionHandler.treatException(e);
            return null;
        }
    }
}
