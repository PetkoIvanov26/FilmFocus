package filmfocus.testUtils.constants;

import java.time.LocalDate;

import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class UserConstants {

  public static final int ID = 1;
  public static final String USERNAME = "Username";
  public static final String PASSWORD = "password";
  public static final String EMAIL = "test@email.com";
  public static final String FIRST_NAME = "First name";
  public static final String LAST_NAME = "Last name";
  public static final int YEAR = 2000;
  public static final int MONTH = 5;
  public static final int DAY = 3;
  public static final LocalDate JOIN_DATE = LocalDate.of(YEAR, MONTH, DAY);

  private UserConstants() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }
}
