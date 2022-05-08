package cz.mff.jassp.processing.logic;

/**
 * Exception which gets thrown when logical invariants are broken during the parsing of arguments.
 */
public class LogicException extends RuntimeException {

    public LogicException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogicException(String message) {
        super(message);
    }

    public LogicException(Throwable cause) {
        super(cause);
    }

    public LogicException() {
        super();
    }
}
