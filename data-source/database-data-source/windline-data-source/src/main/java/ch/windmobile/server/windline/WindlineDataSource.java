package ch.windmobile.server.windline;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.Vector;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.windmobile.server.model.DataSourceException;
import ch.windmobile.server.model.DataSourceException.Error;
import ch.windmobile.server.model.LinearRegression;
import ch.windmobile.server.model.WindMobileDataSource;
import ch.windmobile.server.model.xml.Chart;
import ch.windmobile.server.model.xml.Point;
import ch.windmobile.server.model.xml.Serie;
import ch.windmobile.server.model.xml.StationData;
import ch.windmobile.server.model.xml.StationInfo;
import ch.windmobile.server.model.xml.StationLocationType;
import ch.windmobile.server.model.xml.Status;
import ch.windmobile.server.windline.dataobject.Data;
import ch.windmobile.server.windline.dataobject.DataType;
import ch.windmobile.server.windline.dataobject.Property;
import ch.windmobile.server.windline.dataobject.PropertyValue;
import ch.windmobile.server.windline.dataobject.Station;

public class WindlineDataSource implements WindMobileDataSource {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private static final String STATUS_OFFLINE = "offline";
    private static final String STATUS_DEMO = "demo";

    // 1 hour by default
    private int historicDuration = 60 * 60;
    private int windTrendScale = 500000;

    static enum DataTypeConstant {
        windDirection("winddir1", "windDirection"), windAverage("windspeed1", "windAverage"), windMax("windpeak1", "windMax"), airTemperature(
            "temp1", "airTemperature"), airHumidity("hum1", "airHumidity");

        private final String id;
        private final String name;

        private DataTypeConstant(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    private final SessionFactory sessionFactory;

    public WindlineDataSource(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getSession() {
        return sessionFactory.openSession();
    }

    private int getWindlineStationId(String stationId) {
        return Integer.parseInt(stationId);
    }

    private String getServerStationId(int stationId) {
        return Integer.toString(stationId);
    }

    private Property getPropertyWithKey(Session session, String key) {
        Criteria criteria = session.createCriteria(Property.class);
        criteria.add(Restrictions.eq("key", key));
        return (Property) criteria.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    private List<Station> getStations(Session session) {
        Criteria criteria = session.createCriteria(Station.class);

        criteria.setCacheable(true);
        criteria.setCacheRegion("stationInfos");
        return criteria.list();
    }

    private Station getStation(Session session, String stationId) {
        Criteria criteria = session.createCriteria(Station.class);
        criteria.add(Restrictions.eq("stationId", getWindlineStationId(stationId)));
        criteria.setCacheable(true);
        criteria.setCacheRegion("stationInfos");
        return (Station) criteria.uniqueResult();
    }
    
    private Calendar getLastUpdate(Session session, Station station, DataType dataType) throws DataSourceException {
        Criteria criteria = session.createCriteria(Data.class);
        criteria.add(Restrictions.eq("station", station));
        criteria.add(Restrictions.eq("dataType", dataType));
        criteria.addOrder(Order.desc("time"));
        criteria.setMaxResults(1);
        criteria.setCacheable(true);
        criteria.setCacheRegion("dataQueries");

        Data lastData = (Data) criteria.uniqueResult();
        if ((lastData == null) || (lastData.getTime() == null)) {
            throw new DataSourceException(Error.INVALID_DATA, "No data found for type '" + dataType.getName() + "'");
        }

        Calendar lastUpdate = new GregorianCalendar();
        lastUpdate.setTime(lastData.getTime());
        return lastUpdate;
    }

    private Status getMaintenanceStatus(Session session, Station station) {
        Property propertyStatus = getPropertyWithKey(session, "status");

        Set<PropertyValue> propertyValues = station.getPropertyValues();
        for (PropertyValue propertyValue : propertyValues) {
            if (propertyValue.getProperty() == propertyStatus) {
                if (propertyValue.getValue().equalsIgnoreCase(STATUS_OFFLINE)) {
                    return Status.RED;
                } else if (propertyValue.getValue().equalsIgnoreCase(STATUS_DEMO)) {
                    return Status.ORANGE;
                }
            }
        }
        return Status.GREEN;
    }

    static protected Calendar getExpirationDate(Calendar now, Calendar lastUpdate) {
        TimeZone stationLocalTimeZone = lastUpdate.getTimeZone();
        Calendar expirationDate = Calendar.getInstance(stationLocalTimeZone);

        now.setTimeZone(stationLocalTimeZone);
        expirationDate.setTimeInMillis(lastUpdate.getTimeInMillis() + 10 * 60 * 1000);
        return expirationDate;
    }

    private Status getStatus(Session session, Station station, Calendar now, Calendar expirationDate) {
        // Orange > 10 minutes late
        Date orangeStatusLimit = new Date(expirationDate.getTimeInMillis() + 10 * 60 * 1000);
        // Red > 2h10 late
        Date redStatusLimit = new Date(expirationDate.getTimeInMillis() + 2 * 3600 * 1000 + 10 * 60 * 1000);

        Status maintenanceStatus = getMaintenanceStatus(session, station);

        if ((maintenanceStatus == Status.RED) || (now.getTime().after(redStatusLimit))) {
            return Status.RED;
        } else if ((maintenanceStatus == Status.ORANGE) || (now.getTime().after(orangeStatusLimit))) {
            return Status.ORANGE;
        } else {
            return Status.GREEN;
        }
    }

    /**
     * Return the last value of a sensorId
     * 
     * @param session
     * @param sensorId
     * @return
     * @throws DataSourceException
     */
    private float getData(Session session, Station station, DataType dataType) throws DataSourceException {
        Calendar lastUpdate = getLastUpdate(session, station, dataType);

        Criteria criteria = session.createCriteria(Data.class);
        criteria.add(Restrictions.eq("station", station));
        criteria.add(Restrictions.eq("dataType", dataType));
        criteria.add(Restrictions.eq("time", lastUpdate.getTime()));
        criteria.setCacheable(true);
        criteria.setCacheRegion("dataQueries");

        Data data = (Data) criteria.uniqueResult();
        return Float.parseFloat(data.getValue());
    }

    /**
     * Return a list of Data from (lastUpdate - duration) to lastUpdate
     * 
     * @param session
     * @param sensorId
     * @param duration
     *            The duration in seconds
     * @return
     * @throws DataSourceException
     */
    @SuppressWarnings("unchecked")
    private List<Data> getHistoricData(Session session, Station station, DataType dataType, int duration) throws DataSourceException {
        Calendar lastUpdate = getLastUpdate(session, station, dataType);
        long startTime = lastUpdate.getTimeInMillis() - duration * 1000;

        Criteria criteria = session.createCriteria(Data.class);
        criteria.add(Restrictions.eq("station", station));
        criteria.add(Restrictions.eq("dataType", dataType));
        criteria.add(Restrictions.ge("time", new Date(startTime)));
        criteria.setCacheable(true);
        criteria.setCacheRegion("dataQueries");
        return criteria.list();
    }

    private StationInfo createStationInfo(Session session, Station station) {
        StationInfo stationInfo = new StationInfo();

        stationInfo.setId(getServerStationId(station.getStationId()));
        stationInfo.setShortName(station.getName());
        stationInfo.setName(station.getShortDescription());
        stationInfo.setMaintenanceStatus(getMaintenanceStatus(session, station));
        stationInfo.setStationLocationType(StationLocationType.TAKEOFF);

        Property propertyAltitude = getPropertyWithKey(session, "altitude");
        Property propertyLatitude = getPropertyWithKey(session, "latitude");
        Property propertyLongitude = getPropertyWithKey(session, "longitude");

        Set<PropertyValue> propertyValues = station.getPropertyValues();
        for (PropertyValue propertyValue : propertyValues) {
            if (propertyValue.getProperty() == propertyAltitude) {
                stationInfo.setAltitude(Integer.decode(propertyValue.getValue()));
            } else if (propertyValue.getProperty() == propertyLatitude) {
                stationInfo.setWgs84Latitude(CoordinateHelper.parseDMS(propertyValue.getValue()));
            } else if (propertyValue.getProperty() == propertyLongitude) {
                stationInfo.setWgs84Longitude(CoordinateHelper.parseDMS(propertyValue.getValue()));
            }
        }
        if (stationInfo.getAltitude() == null) {
            stationInfo.setAltitude(-1);
        }

        return stationInfo;
    }

    @Override
    public Calendar getLastUpdate(String stationId) throws DataSourceException {
        Session session = null;
        try {
            session = getSession();
            Station station = getStation(session, stationId);

            DataType dataType = getDataType(session, DataTypeConstant.windAverage);
            return getLastUpdate(session, station, dataType);
        } catch (Exception e) {
            ExceptionHandler.treatException(e);
            return null;
        } finally {
            try {
                session.close();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public List<StationInfo> getStationInfoList(boolean allStation) throws DataSourceException {
        Session session = null;
        try {
            session = getSession();
            List<Station> stations = getStations(session);

            Property propertyType = getPropertyWithKey(session, "type");
            Property propertyStatus = getPropertyWithKey(session, "status");

            List<StationInfo> stationInfoList = new Vector<StationInfo>();
            for (Station station : stations) {
                boolean keepIt = true;
                Set<PropertyValue> propertyValues = station.getPropertyValues();
                for (PropertyValue propertyValue : propertyValues) {
                    if (propertyValue.getProperty() == propertyType) {
                        if (propertyValue.getValue().equals("voice")) {
                            keepIt = false;
                            break;
                        }
                    }
                    if ((allStation == false) && (propertyValue.getProperty() == propertyStatus)) {
                        if (propertyValue.getValue().equalsIgnoreCase(STATUS_OFFLINE) || propertyValue.getValue().equalsIgnoreCase(STATUS_DEMO)) {
                            keepIt = false;
                            break;
                        }
                    }
                }

                if (keepIt) {
                    stationInfoList.add(createStationInfo(session, station));
                }
            }

            return stationInfoList;
        } catch (Exception e) {
            ExceptionHandler.treatException(e);
            return null;
        } finally {
            try {
                session.close();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public StationInfo getStationInfo(String stationId) throws DataSourceException {
        Session session = null;
        try {
            session = getSession();
            Station station = getStation(session, stationId);

            return createStationInfo(session, station);
        } catch (Exception e) {
            ExceptionHandler.treatException(e);
            return null;
        } finally {
            try {
                session.close();
            } catch (Exception e) {
            }
        }
    }

    private DataType getDataType(Session session, DataTypeConstant dataTypeConstant) {
        Criteria criteria = session.createCriteria(DataType.class);
        criteria.add(Restrictions.eq("name", dataTypeConstant.getId()));
        criteria.setCacheable(true);
        return (DataType) criteria.uniqueResult();
    }

    private StationData createStationData(Session session, Station station) throws DataSourceException {
        StationData stationData = new StationData();

        stationData.setStationId(getServerStationId(station.getStationId()));

        // Last update
        DataType dataType = getDataType(session, DataTypeConstant.windAverage);
        Calendar lastUpdate = getLastUpdate(session, station, dataType);
        stationData.setLastUpdate(lastUpdate);

        // Expiration date
        Calendar now = Calendar.getInstance();
        Calendar expirationDate = getExpirationDate(now, lastUpdate);
        stationData.setExpirationDate(expirationDate);

        // Status
        stationData.setStatus(getStatus(session, station, now, expirationDate));

        // Wind average
        dataType = getDataType(session, DataTypeConstant.windAverage);
        double windAverage = getData(session, station, dataType);
        stationData.setWindAverage((float) windAverage);

        // Wind max
        dataType = getDataType(session, DataTypeConstant.windMax);
        double windMax = getData(session, station, dataType);
        stationData.setWindMax((float) windMax);

        // Wind direction chart
        dataType = getDataType(session, DataTypeConstant.windDirection);
        List<Data> windDirectionDatas = getHistoricData(session, station, dataType, getHistoricDuration());
        Serie windDirectionSerie = createSerie(windDirectionDatas);
        windDirectionSerie.setName(DataTypeConstant.windDirection.getName());
        Chart windDirectionChart = new Chart();
        windDirectionChart.setDuration(getHistoricDuration());
        windDirectionChart.getSeries().add(windDirectionSerie);
        stationData.setWindDirectionChart(windDirectionChart);

        // Wind history min/average
        dataType = getDataType(session, DataTypeConstant.windAverage);
        List<Data> windAverageDatas = getHistoricData(session, station, dataType, getHistoricDuration());
        double minValue = Double.MAX_VALUE;
        double sum = 0;
        // double[][] windTrendAverageDatas = new double[windAverageDatas.size()][2];
        for (int i = 0; i < windAverageDatas.size(); i++) {
            Data data = windAverageDatas.get(i);
            float floatValue = Float.parseFloat(data.getValue());
            minValue = Math.min(minValue, floatValue);
            sum += floatValue;
            // windTrendAverageDatas[i][0] = data.getTime().getTime();
            // windTrendAverageDatas[i][1] = data.getValue();
        }
        stationData.setWindHistoryMin((float) minValue);
        stationData.setWindHistoryAverage((float) (sum / windAverageDatas.size()));

        // Wind history max
        dataType = getDataType(session, DataTypeConstant.windMax);
        List<Data> windMaxDatas = getHistoricData(session, station, dataType, getHistoricDuration());
        double maxValue = Double.MIN_VALUE;
        double[][] windTrendMaxDatas = new double[windMaxDatas.size()][2];
        for (int i = 0; i < windMaxDatas.size(); i++) {
            Data data = windMaxDatas.get(i);
            float floatValue = Float.parseFloat(data.getValue());
            maxValue = Math.max(maxValue, floatValue);
            windTrendMaxDatas[i][0] = data.getTime().getTime();
            windTrendMaxDatas[i][1] = floatValue;
        }
        stationData.setWindHistoryMax((float) maxValue);

        // Wind trend
        LinearRegression linearRegression = new LinearRegression(windTrendMaxDatas);
        linearRegression.compute();
        double slope = linearRegression.getBeta1();
        double angle = Math.toDegrees(Math.atan(slope * getWindTrendScale()));
        stationData.setWindTrend((int) angle);

        // Air temperature
        dataType = getDataType(session, DataTypeConstant.airTemperature);
        double airTemperature = getData(session, station, dataType);
        stationData.setAirTemperature((float) airTemperature);

        // Air humidity
        dataType = getDataType(session, DataTypeConstant.airHumidity);
        double airHumidity = getData(session, station, dataType);
        stationData.setAirHumidity((float) airHumidity);

        return stationData;
    }

    @Override
    public StationData getStationData(String stationId) throws DataSourceException {
        Session session = null;
        try {
            session = getSession();
            Station station = getStation(session, stationId);

            return createStationData(session, station);
        } catch (Exception e) {
            ExceptionHandler.treatException(e);
            return null;
        } finally {
            try {
                session.close();
            } catch (Exception e) {
            }
        }
    }

    private Serie createSerie(List<Data> datas) {
        Serie serie = new Serie();
        for (Data data : datas) {
            Point newPoint = new Point();
            newPoint.setDate(data.getTime().getTime());
            newPoint.setValue(Float.parseFloat(data.getValue()));
            serie.getPoints().add(newPoint);
        }
        return serie;
    }

    @Override
    public Chart getWindChart(String stationId, int duration) throws DataSourceException {
        Session session = null;
        try {
            session = getSession();
            Station station = getStation(session, stationId);

            Chart windChart = new Chart();

            // Last update
            DataType dataType = getDataType(session, DataTypeConstant.windAverage);
            Calendar lastUpdate = getLastUpdate(session, station, dataType);
            windChart.setLastUpdate(lastUpdate);

            // Wind historic chart
            dataType = getDataType(session, DataTypeConstant.windAverage);
            List<Data> windAverageDatas = getHistoricData(session, station, dataType, duration);
            Serie windAverageSerie = createSerie(windAverageDatas);
            windAverageSerie.setName(DataTypeConstant.windAverage.getName());
            dataType = getDataType(session, DataTypeConstant.windMax);
            List<Data> windMaxDatas = getHistoricData(session, station, dataType, duration);
            Serie windMaxSerie = createSerie(windMaxDatas);
            windMaxSerie.setName(DataTypeConstant.windMax.getName());
            dataType = getDataType(session, DataTypeConstant.windDirection);
            List<Data> windDirectionDatas = getHistoricData(session, station, dataType, duration);
            Serie windDirectionSerie = createSerie(windDirectionDatas);
            windDirectionSerie.setName(DataTypeConstant.windDirection.getName());
            windChart.setDuration(duration);
            windChart.getSeries().add(windAverageSerie);
            windChart.getSeries().add(windMaxSerie);
            windChart.getSeries().add(windDirectionSerie);

            return windChart;
        } catch (Exception e) {
            ExceptionHandler.treatException(e);
            return null;
        } finally {
            try {
                session.close();
            } catch (Exception e) {
            }
        }
    }

    public int getHistoricDuration() {
        return historicDuration;
    }

    public void setHistoricDuration(int historicDuration) {
        this.historicDuration = historicDuration;
    }

    public int getWindTrendScale() {
        return windTrendScale;
    }

    public void setWindTrendScale(int windTrendScale) {
        this.windTrendScale = windTrendScale;
    }
}
