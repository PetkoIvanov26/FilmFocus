package filmfocus.exceptions;

public class NoAvailableTicketsException extends RuntimeException {

  public NoAvailableTicketsException(String message) {
    super(message);
  }
}
