package ch.windmobile.server.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

@Path("/test")
public class TestResource {

    @Context
    UriInfo uriInfo;
    @Context
    Request request;

    @GET
    @Path("exception")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public int throwsException() {
        try {
            return 0 / 0;
        } catch (Throwable e) {
            ExceptionHandler.treatException(e);
            return 0;
        }
    }
}