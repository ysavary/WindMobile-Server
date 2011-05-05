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
