package ch.windmobile.server.datasourcemodel;

import ch.windmobile.server.datasourcemodel.DataSourceException.Error;

class AggregatedId {
    public static final String SEPARATOR = ":";

    private final String dataSourceKey;
    private final String stationId;

    AggregatedId(String id) throws DataSourceException {
        int index = id.indexOf(SEPARATOR);
        if (index == -1) {
            throw new DataSourceException(Error.SERVER_ERROR, "Could not find aggregated id separator '" + SEPARATOR
                + "' in '" + id + "'");
        }
        dataSourceKey = id.substring(0, index);
        stationId = id.substring(index + 1);
    }

    String getDataSourceKey() {
        return dataSourceKey;
    }

    String getStationId() {
        return stationId;
    }

    static String toString(String dataSourceKey, String stationId) {
        StringBuffer str = new StringBuffer(dataSourceKey);
        str.append(SEPARATOR);
        str.append(stationId);
        return str.toString();
    }

    @Override
    public String toString() {
        return toString(getDataSourceKey(), getStationId());
    }
}