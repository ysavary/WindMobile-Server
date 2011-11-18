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
package ch.windmobile.server.windline;

import org.hibernate.HibernateException;
import org.hibernate.exception.JDBCConnectionException;

import ch.windmobile.server.datasourcemodel.DataSourceException;
import ch.windmobile.server.datasourcemodel.DataSourceException.Error;

public class ExceptionHandler {

    static void treatException(Throwable e) throws DataSourceException {
        if (e instanceof DataSourceException) {
            throw (DataSourceException) e;
        } else if (e instanceof JDBCConnectionException) {
            throw new DataSourceException(Error.CONNECTION_ERROR, e.getCause());
        } else if (e instanceof HibernateException) {
            throw new DataSourceException(Error.DATABASE_ERROR, e);
        }
        throw new DataSourceException(Error.SERVER_ERROR, e);
    }
}
