package ch.windmobile.server.resource;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import ch.windmobile.server.model.WindMobileDataSource;

import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.spi.resource.Singleton;

@Path("/stationdatas")
@Singleton
public class StationDataListResource {
    @InjectParam("dataSource")
    private WindMobileDataSource dataSource;

    @Context
    UriInfo uriInfo;
    @Context
    Request request;

    @Path("{stationId}")
    public StationDataResource getMain(@PathParam("stationId") String stationId) {
        return new StationDataResource(uriInfo, request, stationId, dataSource);
    }
}
