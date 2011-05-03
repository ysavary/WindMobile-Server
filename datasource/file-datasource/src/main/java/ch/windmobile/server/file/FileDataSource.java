package ch.windmobile.server.file;

import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import ch.windmobile.server.datasourcemodel.DataSourceException;
import ch.windmobile.server.datasourcemodel.WindMobileDataSource;
import ch.windmobile.server.datasourcemodel.xml.Chart;
import ch.windmobile.server.datasourcemodel.xml.StationData;
import ch.windmobile.server.datasourcemodel.xml.StationDatas;
import ch.windmobile.server.datasourcemodel.xml.StationInfo;
import ch.windmobile.server.datasourcemodel.xml.StationInfos;
import ch.windmobile.server.datasourcemodel.xml.StationUpdateTime;

public class FileDataSource implements WindMobileDataSource {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final Resource stationInfosResource;
    private final Resource stationDatasResource;
    private final Resource windChartResource;

    private final DateTime lastUpdate;
    private final JAXBContext jaxbContext;

    public FileDataSource(Resource stationInfosResource, Resource stationDatasResource, Resource windChartResource) throws JAXBException {
        this.stationInfosResource = stationInfosResource;
        this.stationDatasResource = stationDatasResource;
        this.windChartResource = windChartResource;

        lastUpdate = new DateTime();
        jaxbContext = JAXBContext.newInstance("ch.windmobile.server.model.xml");
    }

    public Resource getStationInfosResource() {
        return stationInfosResource;
    }

    public Resource getStationDatasResource() {
        return stationDatasResource;
    }

    public Resource getWindChartResource() {
        return windChartResource;
    }

    @Override
    public StationUpdateTime getLastUpdate(String stationId) throws DataSourceException {
        StationUpdateTime returnObject = new StationUpdateTime();
        returnObject.setLastUpdate(lastUpdate);
        return returnObject;
    }

    @Override
    public List<StationInfo> getStationInfoList(boolean allStation) throws DataSourceException {
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StationInfos stationInfos = (StationInfos) unmarshaller.unmarshal(getStationInfosResource().getInputStream());
            return stationInfos.getStationInfos();
        } catch (Exception e) {
            throw new DataSourceException(DataSourceException.Error.INVALID_DATA, "Unable to parse the test data", e);
        }
    }

    @Override
    public StationInfo getStationInfo(String stationId) throws DataSourceException {
        List<StationInfo> stationInfoList = getStationInfoList(true);
        for (StationInfo stationInfo : stationInfoList) {
            if (stationInfo.getId().equals(stationId)) {
                return stationInfo;
            }
        }
        return null;
    }

    public List<StationData> getStationDataList(boolean allStation) throws DataSourceException {
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StationDatas stationDatas = (StationDatas) unmarshaller.unmarshal(getStationDatasResource().getInputStream());
            return stationDatas.getStationDatas();
        } catch (Exception e) {
            throw new DataSourceException(DataSourceException.Error.INVALID_DATA, "Unable to parse the test data", e);
        }
    }

    @Override
    public StationData getStationData(String stationId) throws DataSourceException {
        List<StationData> stationDataList = getStationDataList(true);
        for (StationData stationData : stationDataList) {
            if (stationData.getStationId().equals(stationId)) {
                return stationData;
            }
        }
        return null;
    }

    @Override
    public Chart getWindChart(String stationId, int duration) throws DataSourceException {
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Chart windChart = (Chart) unmarshaller.unmarshal(getWindChartResource().getInputStream());
            return windChart;
        } catch (Exception e) {
            throw new DataSourceException(DataSourceException.Error.INVALID_DATA, "Unable to parse the test data", e);
        }
    }
}
