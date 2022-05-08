package cz.mff.jassp.parser;

/**
 * Exception related to the run-time parsing of arguments.
 */
public class ParserException extends RuntimeException {

    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParserException(String message) {
        super(message);
    }

    public ParserException(Throwable cause) {
        super(cause);
    }

    public ParserException() {
        super();
    }
}
