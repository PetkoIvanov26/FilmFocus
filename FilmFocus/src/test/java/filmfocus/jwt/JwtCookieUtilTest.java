package filmfocus.jwt;

import filmfocus.testUtils.factories.JwtFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpCookie;

import static filmfocus.testUtils.constants.JwtConstants.JWT_COOKIE_NAME;
import static filmfocus.testUtils.constants.JwtConstants.JWT_TOKEN;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JwtCookieUtilTest {

  @Mock
  private JwtTokenUtil jwtTokenUtil;

  @InjectMocks
  private JwtCookieUtil jwtCookieUtil;

  @Test
  public void testCreateJWTCookie_cookieCreated_success() {
    when(jwtTokenUtil.generateToken(any())).thenReturn(JWT_TOKEN);

    HttpCookie httpCookie = jwtCookieUtil.createJWTCookie(JwtFactory.getDefaultUserDetails());

    assertEquals(JWT_COOKIE_NAME, httpCookie.getName());
    assertEquals(JWT_TOKEN, httpCookie.getValue());
  }
}