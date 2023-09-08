package filmfocus.exceptions;

public class DiscountNotFoundException extends RuntimeException {

  public DiscountNotFoundException(String message) {
    super(message);
  }
}
