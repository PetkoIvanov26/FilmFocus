package filmfocus.exceptions;

public class MovieAlreadyExistsException extends IllegalArgumentException {

  public MovieAlreadyExistsException(String message) {
    super(message);
  }
}
