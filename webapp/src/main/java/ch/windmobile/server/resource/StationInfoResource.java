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

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import ch.windmobile.server.datasourcemodel.WindMobileDataSource;
import ch.windmobile.server.datasourcemodel.xml.StationInfo;

import com.sun.jersey.api.NotFoundException;

public class StationInfoResource {
    private WindMobileDataSource dataSource;

    UriInfo uriInfo;
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
