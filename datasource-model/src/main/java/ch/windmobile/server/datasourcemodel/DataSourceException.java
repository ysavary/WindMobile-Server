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

public class DataSourceException extends Exception {
    private static final long serialVersionUID = 1L;

    public static enum Error {
        SERVER_ERROR(-1), DATABASE_ERROR(-2), INVALID_DATA(-3), CONNECTION_ERROR(-4), UNAUTHORIZED(-5);

        private final int code;

        private Error(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    private final Error error;

    public DataSourceException(Error error, String message) {
        super(message);
        this.error = error;
    }

    public DataSourceException(Error error, Throwable cause) {
        super(cause);
        this.error = error;
    }

    public DataSourceException(Error error, String message, Throwable cause) {
        super(message, cause);
        this.error = error;
    }

    public Error getError() {
        return error;
    }

    @Override
    public String toString() {
        String className = getClass().getName();
        return className + "<" + getError() + ">: " + getLocalizedMessage();
    }
}
