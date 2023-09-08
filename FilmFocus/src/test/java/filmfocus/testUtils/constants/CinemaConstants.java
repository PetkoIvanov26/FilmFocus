package filmfocus.testUtils.constants;

import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class CinemaConstants {

  public static final int ID = 1;
  public static final String ADDRESS = "Address";
  public static final String CITY = "City";
  public static final int AVERAGE_RATING = 5;

  private CinemaConstants() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }
}
