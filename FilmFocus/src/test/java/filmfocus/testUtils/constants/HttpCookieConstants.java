package filmfocus.testUtils.constants;

import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class HttpCookieConstants {

  public static final String COOKIE_NAME = "Cookie";
  public static final String COOKIE_VALUE = "Value";

  private HttpCookieConstants() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }
}