package ch.windmobile.server.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import ch.windmobile.server.model.WindMobileDataSource;
import ch.windmobile.server.model.xml.StationInfo;

import com.sun.jersey.api.NotFoundException;

public class StationInfoResource {
    private WindMobileDataSource dataSource;

    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    String id;

    public StationInfoResource(UriInfo uriInfo, Request request, String id, WindMobileDataSource dataSource) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.id = id;
        this.dataSource = dataSource;
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public StationInfo getStationInfo() {
        try {
            StationInfo stationInfo = dataSource.getStationInfo(id);
            if (stationInfo != null) {
                return stationInfo;
            } else {
                throw new NotFoundException("No such StationInfo with id '" + id + "'");
            }
        } catch (Exception e) {
            ExceptionHandler.treatException(e);
            return null;
        }
    }
}
