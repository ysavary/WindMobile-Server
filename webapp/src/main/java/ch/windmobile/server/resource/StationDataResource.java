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
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import ch.windmobile.server.datasourcemodel.WindMobileDataSource;
import ch.windmobile.server.datasourcemodel.xml.StationData;

import com.sun.jersey.api.NotFoundException;

public class StationDataResource {
    private WindMobileDataSource dataSource;

    UriInfo uriInfo;
    HttpServletRequest request;
    String stationId;

    public StationDataResource(UriInfo uriInfo, HttpServletRequest request, String stationId, WindMobileDataSource dataSource) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.stationId = stationId;
        this.dataSource = dataSource;
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public StationData getStationData() {
        try {
            StationData stationData = dataSource.getStationData(stationId);
            if (stationData != null) {
                return stationData;
            } else {
                throw new NotFoundException("No such StationData with id '" + stationId + "'");
            }
        } catch (Exception e) {
            throw ExceptionHandler.treatException(e, request);
        }
    }
}
