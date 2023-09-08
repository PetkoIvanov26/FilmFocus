package filmfocus.exceptions;

public class NotLoggedInException extends RuntimeException {

  public NotLoggedInException(String message) {
    super(message);
  }
}
