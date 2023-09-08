package filmfocus.exceptions;

public class CinemaAlreadyExistsException extends RuntimeException{
  public CinemaAlreadyExistsException(String message){
    super(message);
  }
}
