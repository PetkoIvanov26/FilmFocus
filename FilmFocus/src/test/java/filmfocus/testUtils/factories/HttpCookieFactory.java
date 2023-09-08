package filmfocus.testUtils.factories;

import org.springframework.http.HttpCookie;

import static filmfocus.testUtils.constants.HttpCookieConstants.COOKIE_NAME;
import static filmfocus.testUtils.constants.HttpCookieConstants.COOKIE_VALUE;

public final class HttpCookieFactory {

  private HttpCookieFactory() throws IllegalAccessException {
    throw new IllegalAccessException();
  }

  public static HttpCookie getDefaultHttpCookie() {
    return new HttpCookie(COOKIE_NAME, COOKIE_VALUE);
  }
}
