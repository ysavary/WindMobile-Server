package ch.windmobile.server.resource;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.windmobile.server.model.DataSourceException;
import ch.windmobile.server.model.xml.Error;

public class ExceptionHandler {
    protected static final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);

    private static String printStacktrace(Throwable e) {
        if (e.getStackTrace() != null) {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            e.printStackTrace(printWriter);
            return writer.toString();
        }
        return "";
    }

    private static void logError(Throwable e) {
        log.error("WindMobile exception:", e);
    }

    static void treatException(Throwable e) throws WebApplicationException {
        logError(e);

        if (e instanceof WebApplicationException) {
            throw (WebApplicationException) e;
        } else if (e instanceof DataSourceException) {
            DataSourceException dataSourceException = (DataSourceException) e;

            Error error = new Error();
            error.setCode(dataSourceException.getError().getCode());
            if (dataSourceException.getMessage() != null) {
                error.setMessage(dataSourceException.getMessage());
            } else if (dataSourceException.getCause() != null) {
                error.setMessage(dataSourceException.getCause().getMessage());
            }
            error.setStacktrace(printStacktrace(dataSourceException));

            Status httpStatus;
            switch (dataSourceException.getError()) {
            case CONNECTION_ERROR:
                httpStatus = Status.SERVICE_UNAVAILABLE;
                break;
                
            case INVALID_DATA:
                httpStatus = Status.BAD_REQUEST;
                break;

            case DATABASE_ERROR:
                httpStatus = Status.INTERNAL_SERVER_ERROR;
                break;

            default:
                httpStatus = Status.INTERNAL_SERVER_ERROR;
                break;
            }

            throw new WebApplicationException(Response.status(httpStatus).entity(error).build());
        } else {
            Error error = new Error();
            error.setCode(DataSourceException.Error.SERVER_ERROR.getCode());
            if (e.getMessage() != null) {
                error.setMessage(e.getMessage());
            } else if (e.getCause() != null) {
                error.setMessage(e.getCause().getMessage());
            }
            error.setStacktrace(printStacktrace(e));

            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity(error).build());
        }
    }
}
