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
