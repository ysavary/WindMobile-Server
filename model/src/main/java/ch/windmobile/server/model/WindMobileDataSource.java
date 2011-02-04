package ch.windmobile.server.model;

import java.util.Calendar;
import java.util.List;

import ch.windmobile.server.model.xml.Chart;
import ch.windmobile.server.model.xml.StationData;
import ch.windmobile.server.model.xml.StationInfo;

public interface WindMobileDataSource {
    
    /**
     * @param stationId
     * @return
     * @throws DataSourceException
     */
    public Calendar getLastUpdate(String stationId) throws DataSourceException;
    
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
