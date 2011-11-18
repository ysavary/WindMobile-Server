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
package ch.windmobile.server.datasourcemodel;

import java.util.List;

import ch.windmobile.server.datasourcemodel.xml.Chart;
import ch.windmobile.server.datasourcemodel.xml.StationData;
import ch.windmobile.server.datasourcemodel.xml.StationInfo;
import ch.windmobile.server.datasourcemodel.xml.StationUpdateTime;

public interface WindMobileDataSource {
    
    /**
     * @param stationId
     * @return
     * @throws DataSourceException
     */
    public StationUpdateTime getLastUpdate(String stationId) throws DataSourceException;
    
	/**
	 * @param allStation
	 * By default (false) returns only the active stations
	 * @return
	 * @throws DataSourceException
	 */
	public List<StationInfo> getStationInfoList(boolean allStation) throws DataSourceException;
	
	/**
	 * @param stationId
	 * @return
	 * @throws DataSourceException
	 */
	public StationInfo getStationInfo(String stationId) throws DataSourceException;
	
	/**
	 * @param stationId
	 * @return
	 * @throws DataSourceException
	 */
	public StationData getStationData(String stationId) throws DataSourceException;
	
	/**
	 * @param stationId
	 * @param duration in seconds
	 * @return
	 * @throws DataSourceException
	 */
	public Chart getWindChart(String stationId, int duration) throws DataSourceException;	
}
