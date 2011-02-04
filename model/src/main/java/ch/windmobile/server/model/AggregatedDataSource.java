package ch.windmobile.server.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.windmobile.server.model.DataSourceException.Error;
import ch.windmobile.server.model.xml.Chart;
import ch.windmobile.server.model.xml.StationData;
import ch.windmobile.server.model.xml.StationInfo;

public class AggregatedDataSource implements WindMobileDataSource {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final int timeout;
    
    private final ExecutorService executor;
    private Map<String, WindMobileDataSource> dataSources;

    public AggregatedDataSource(int corePoolSize, int maximumPoolSize, int timeout) {
        this.timeout = timeout;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, timeout, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public Map<String, WindMobileDataSource> getDataSources() {
        return dataSources;
    }

    public void setDataSources(Map<String, WindMobileDataSource> dataSources) {
        this.dataSources = dataSources;
    }

    private DataSourceException exceptionHandler(Exception e) {
        if (e instanceof DataSourceException) {
            return (DataSourceException) e;
        } else {
            return new DataSourceException(Error.SERVER_ERROR, e);
        }
    }

    @Override
    public Calendar getLastUpdate(String stationId) throws DataSourceException {
        try {
            AggregatedId aggregatedId = new AggregatedId(stationId);
            WindMobileDataSource dataSource = getDataSources().get(aggregatedId.getDataSourceKey());
            return dataSource.getLastUpdate(aggregatedId.getStationId());
        } catch (Exception e) {
            throw exceptionHandler(e);
        }
    }

    @Override
    public List<StationInfo> getStationInfoList(final boolean allStation) throws DataSourceException {
        try {
            List<AggregatedCallable<List<StationInfo>>> callables = new ArrayList<AggregatedCallable<List<StationInfo>>>();

            Set<String> keys = getDataSources().keySet();
            for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
                String key = iterator.next();

                final WindMobileDataSource dataSource = getDataSources().get(key);
                AggregatedCallable<List<StationInfo>> callable = new AggregatedCallable<List<StationInfo>>(key) {
                    @Override
                    public List<StationInfo> call() throws Exception {
                        return dataSource.getStationInfoList(allStation);
                    }
                };
                callables.add(callable);
            }

            List<Future<List<StationInfo>>> futures = executor.invokeAll(callables, timeout, TimeUnit.SECONDS);

            List<StationInfo> aggregatedStationInfos = new ArrayList<StationInfo>(futures.size());
            for (int i = 0; i < futures.size(); i++) {
                Future<List<StationInfo>> future = futures.get(i);
                try {
                    List<StationInfo> stationInfos = future.get();
                    // Replace the id by the aggregated id
                    for (StationInfo stationInfo : stationInfos) {
                        stationInfo.setId(AggregatedId.toString(callables.get(i).getDataSourceKey(),
                            stationInfo.getId()));
                    }
                    aggregatedStationInfos.addAll(stationInfos);
                } catch (ExecutionException e) {
                    log.warn("Could not get StationInfo list:", e);
                } catch (InterruptedException e) {
                    log.warn("Could not get StationInfo list:", e);
                } catch (CancellationException e) {
                    log.warn("Could not get StationInfo list:", e);
                }                
            }
            return aggregatedStationInfos;
        } catch (Exception e) {
            throw exceptionHandler(e);
        }
    }

    @Override
    public StationInfo getStationInfo(String stationId) throws DataSourceException {
        try {
            AggregatedId aggregatedId = new AggregatedId(stationId);
            WindMobileDataSource dataSource = getDataSources().get(aggregatedId.getDataSourceKey());
            return dataSource.getStationInfo(aggregatedId.getStationId());
        } catch (Exception e) {
            throw exceptionHandler(e);
        }
    }

    @Override
    public StationData getStationData(String stationId) throws DataSourceException {
        try {
            AggregatedId aggregatedId = new AggregatedId(stationId);
            WindMobileDataSource dataSource = getDataSources().get(aggregatedId.getDataSourceKey());
            return dataSource.getStationData(aggregatedId.getStationId());
        } catch (Exception e) {
            throw exceptionHandler(e);
        }
    }

    @Override
    public Chart getWindChart(String stationId, int duration) throws DataSourceException {
        try {
            AggregatedId aggregatedId = new AggregatedId(stationId);
            WindMobileDataSource dataSource = getDataSources().get(aggregatedId.getDataSourceKey());
            return dataSource.getWindChart(aggregatedId.getStationId(), duration);
        } catch (Exception e) {
            throw exceptionHandler(e);
        }
    }
}
