package cz.mff.jassp.processing.extractor;

/**
 * Exception which gets thrown during invalid call of extractor on top of a string.
 * This is almost exclusively when {@code validate} check isn't used beforehand.
 */
public class ExtractorException extends RuntimeException {

    public ExtractorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExtractorException(String message) {
        super(message);
    }

    public ExtractorException(Throwable cause) {
        super(cause);
    }

    public ExtractorException() {
        super();
    }
}
