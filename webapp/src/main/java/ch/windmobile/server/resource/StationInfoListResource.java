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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
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
    HttpServletRequest request;

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public StationInfos getStationInfoList(@QueryParam("allStation") boolean allStation) {
        try {
            StationInfos stationInfos = new StationInfos();
            stationInfos.getStationInfos().addAll(dataSource.getStationInfoList(allStation));
            return stationInfos;
        } catch (Exception e) {
            throw ExceptionHandler.treatException(e, request);
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
