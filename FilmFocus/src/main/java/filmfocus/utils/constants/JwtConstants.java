package filmfocus.utils.constants;

import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class JwtConstants {

  public static final long JWT_TOKEN_VALIDITY = 1000 * 60 * 60;
  public static final String JWT_COOKIE_NAME = "JwtCookie";

  private JwtConstants() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }
}