package ch.windmobile.server.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import ch.windmobile.server.datasourcemodel.WindMobileDataSource;
import ch.windmobile.server.datasourcemodel.xml.ObjectFactory;
import ch.windmobile.server.datasourcemodel.xml.StationUpdateTime;

import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.spi.resource.Singleton;

@Path("/lastupdate")
@Singleton
public class LastUpdateResource {
    @InjectParam("dataSource")
    private WindMobileDataSource dataSource;

    @Context
    UriInfo uriInfo;
    @Context
    Request request;

    @GET
    @Path("{stationId}")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public JAXBElement<StationUpdateTime> getLastUpdate(@PathParam("stationId") String stationId) {
        try {
            return new ObjectFactory().createLastUpdate(dataSource.getLastUpdate(stationId));
        } catch (Exception e) {
            ExceptionHandler.treatException(e);
            return null;
        }
    }
}
