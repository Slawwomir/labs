package rest.resource.exceptions;

public class IssueNotFoundException extends RuntimeException {
    public IssueNotFoundException() {
        super();
    }

    public IssueNotFoundException(String message) {
        super(message);
    }

    public IssueNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
