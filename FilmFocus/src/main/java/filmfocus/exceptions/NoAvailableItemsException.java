package filmfocus.exceptions;

public class NoAvailableItemsException extends RuntimeException {

  public NoAvailableItemsException(String message) {
    super(message);
  }
}
