package uk.co.lingzone.flight.exception;

public class CSVLoaderException extends RuntimeException {

    public CSVLoaderException() {}

    public CSVLoaderException(String message) {
        super(message);
    }

    public CSVLoaderException(Throwable cause) {
        super(cause);
    }

    public CSVLoaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public CSVLoaderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
