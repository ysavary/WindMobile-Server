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