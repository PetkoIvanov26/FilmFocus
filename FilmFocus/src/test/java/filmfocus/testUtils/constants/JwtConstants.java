package filmfocus.testUtils.constants;

import java.util.Date;

import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class JwtConstants {

  public static final String JWT_TOKEN = "JwtToken";
  public static final String USER_ROLE = "ADMIN";
  public static final String JWT_PASSWORD = "Password";
  public static final String JWT_USERNAME = "Username";
  public static final String JWT_COOKIE_NAME = "JwtCookie";
  public static final Date NOW = new Date();
  private static final long validityInMilliseconds = 3600000;
  public static final Date EXPIRATION = new Date(NOW.getTime() + validityInMilliseconds);

  private JwtConstants() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }
}