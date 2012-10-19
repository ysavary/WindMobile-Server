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
package ch.windmobile.server.jdc;

import java.net.UnknownHostException;
import java.util.List;

import ch.windmobile.server.datasourcemodel.DataSourceException;
import ch.windmobile.server.datasourcemodel.WindMobileDataSource;
import ch.windmobile.server.datasourcemodel.xml.Chart;
import ch.windmobile.server.datasourcemodel.xml.StationData;
import ch.windmobile.server.datasourcemodel.xml.StationInfo;
import ch.windmobile.server.datasourcemodel.xml.StationUpdateTime;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class JdcDataSource implements WindMobileDataSource {

    private Mongo mongoService;
    private DB database;

    public JdcDataSource() {
        try {
            mongoService = new Mongo();
        } catch (UnknownHostException e) {
        }
        database = mongoService.getDB("windmobile");
    }

    @Override
    public StationUpdateTime getLastUpdate(String stationId) throws DataSourceException {
        DBCollection collection = database.getCollection("stations");
        DBObject station = collection.findOne(BasicDBObjectBuilder.start("_id", stationId).get());
        Object lastMeasurements = station.get("last-measurements");

        return new StationUpdateTime();
    }

    @Override
    public List<StationInfo> getStationInfoList(boolean allStation) throws DataSourceException {
        return null;
    }

    @Override
    public StationInfo getStationInfo(String stationId) throws DataSourceException {
        return null;
    }

    @Override
    public StationData getStationData(String stationId) throws DataSourceException {
        return null;
    }

    @Override
    public Chart getWindChart(String stationId, int duration) throws DataSourceException {
        return null;
    }
}
