package cryptopals.exceptions;

public class ECBException extends RuntimeException {
    public ECBException() {}

    public ECBException(final String message) {
        super(message);
    }

    public ECBException(final String message, final Throwable t) {
        super(message, t);
    }
}
