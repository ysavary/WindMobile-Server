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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

import ch.windmobile.server.datasourcemodel.DataSourceException.Error;
import ch.windmobile.server.datasourcemodel.xml.Chart;
import ch.windmobile.server.datasourcemodel.xml.StationData;
import ch.windmobile.server.datasourcemodel.xml.StationInfo;
import ch.windmobile.server.datasourcemodel.xml.StationUpdateTime;

public class AggregatedDataSource implements WindMobileDataSource {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final int timeout;

    private final ExecutorService executor;
    private Map<String, WindMobileDataSource> dataSources;

    public AggregatedDataSource(int corePoolSize, int maximumPoolSize, int timeout) {
        this.timeout = timeout;
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, timeout, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
            new ThreadPoolExecutor.CallerRunsPolicy());
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
    public StationUpdateTime getLastUpdate(String stationId) throws DataSourceException {
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
                        stationInfo.setId(AggregatedId.toString(callables.get(i).getDataSourceKey(), stationInfo.getId()));
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

            Collections.sort(aggregatedStationInfos, new Comparator<StationInfo>() {
                @Override
                public int compare(StationInfo stationInfo1, StationInfo stationInfo2) {
                    try {
                        return stationInfo1.getShortName().compareTo(stationInfo2.getShortName());
                    } catch (Exception e) {
                        return 0;
                    }
                }
            });
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
            StationInfo stationInfo = dataSource.getStationInfo(aggregatedId.getStationId());
            stationInfo.setId(AggregatedId.toString(aggregatedId.getDataSourceKey(), stationInfo.getId()));
            return stationInfo;
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
