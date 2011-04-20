package ch.windmobile.server.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import ch.windmobile.server.datasourcemodel.WindMobileDataSource;
import ch.windmobile.server.datasourcemodel.xml.StationData;

import com.sun.jersey.api.NotFoundException;

public class StationDataResource {
    private WindMobileDataSource dataSource;
    
    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    String stationId;

    public StationDataResource(UriInfo uriInfo, Request request, String stationId, WindMobileDataSource dataSource) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.stationId = stationId;
        this.dataSource = dataSource;
    }

    @GET
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public StationData getStationData() {
        try {
            StationData stationData = dataSource.getStationData(stationId);
            if (stationData != null) {
                return stationData;
            } else {
                throw new NotFoundException("No such StationData with id '" + stationId + "'");
            }
        } catch (Exception e) {
            ExceptionHandler.treatException(e);
            return null;
        }
    }
}
