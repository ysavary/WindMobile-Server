package ch.windmobile.server.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import ch.windmobile.server.datasourcemodel.WindMobileDataSource;
import ch.windmobile.server.datasourcemodel.xml.StationInfos;

import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.spi.resource.Singleton;

@Path("/stationinfos")
@Singleton
public class StationInfoListResource {
    @InjectParam("dataSource")
    private WindMobileDataSource dataSource;

    @Context
    UriInfo uriInfo;
    @Context
    Request request;

    @GET
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public StationInfos getStationInfoList(@QueryParam("allStation") boolean allStation) {
        try {
            StationInfos stationInfos = new StationInfos();
            stationInfos.getStationInfos().addAll(dataSource.getStationInfoList(allStation));
            return stationInfos;
        } catch (Exception e) {
            ExceptionHandler.treatException(e);
            return null;
        }
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String getCount(@QueryParam("allStation") boolean allStation) {
        int count = getStationInfoList(allStation).getStationInfos().size();
        return String.valueOf(count);
    }

    @Path("{stationId}")
    public StationInfoResource getStationInfo(@PathParam("stationId") String stationId) {
        return new StationInfoResource(uriInfo, request, stationId, dataSource);
    }
}
