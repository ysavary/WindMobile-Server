package ch.windmobile.server.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import ch.windmobile.server.model.DataSourceException;
import ch.windmobile.server.model.WindMobileDataSource;
import ch.windmobile.server.model.xml.Chart;
import ch.windmobile.server.model.xml.Error;

import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.spi.resource.Singleton;

@Path("/windchart")
@Singleton
public class WindChartResource {
    @InjectParam("dataSource")
    private WindMobileDataSource dataSource;

    @Context
    UriInfo uriInfo;
    @Context
    Request request;

    @GET
    @Path("{stationId}/{duration}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Chart getWindChart(@PathParam("stationId") String stationId, @PathParam("duration") int duration) {
        try {
            // Don't kill the server, max 2 days
            if (duration > 2 * 24 * 60 * 60) {
                Error error = new Error();
                error.setCode(DataSourceException.Error.SERVER_ERROR.getCode());
                error.setMessage("Chart duration requested is too long");
                throw new WebApplicationException(Response.status(Status.NOT_ACCEPTABLE).entity(error).build());
            }
            return dataSource.getWindChart(stationId, duration);
        } catch (Exception e) {
            ExceptionHandler.treatException(e);
            return null;
        }
    }
}
