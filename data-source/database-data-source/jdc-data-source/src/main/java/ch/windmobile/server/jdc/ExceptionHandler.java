package ch.windmobile.server.jdc;

import org.hibernate.HibernateException;

import ch.windmobile.server.model.DataSourceException;
import ch.windmobile.server.model.DataSourceException.Error;

public class ExceptionHandler {

    static void treatException(Throwable e) throws DataSourceException {
        if (e instanceof DataSourceException) {
            throw (DataSourceException) e;
        } else if (e instanceof HibernateException) {
            throw new DataSourceException(Error.DATABASE_ERROR, e);
        }
        throw new DataSourceException(Error.SERVER_ERROR, e);
    }
}
