package cz.mff.jassp.option;

/**
 * ParserException which gets thrown during the building process of individual options.
 */
public class BuilderException extends RuntimeException {

    public BuilderException(String message, Throwable cause) {
        super(message, cause);
    }

    public BuilderException(String message) {
        super(message);
    }

    public BuilderException(Throwable cause) {
        super(cause);
    }

    public BuilderException() {
        super();
    }
}
