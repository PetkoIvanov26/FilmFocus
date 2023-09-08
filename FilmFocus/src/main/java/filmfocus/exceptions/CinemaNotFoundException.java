package filmfocus.exceptions;

public class CinemaNotFoundException extends RuntimeException {

  public CinemaNotFoundException(String message) {
    super(message);
  }
}