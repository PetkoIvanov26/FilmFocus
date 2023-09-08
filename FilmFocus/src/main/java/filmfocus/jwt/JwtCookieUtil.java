package filmfocus.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import static filmfocus.utils.constants.JwtConstants.JWT_COOKIE_NAME;
import static filmfocus.utils.constants.JwtConstants.JWT_TOKEN_VALIDITY;

@Component
public class JwtCookieUtil {

  private final Logger log = LoggerFactory.getLogger(JwtCookieUtil.class);

  private final JwtTokenUtil tokenUtil;

  @Autowired
  public JwtCookieUtil(JwtTokenUtil tokenUtil) {
    this.tokenUtil = tokenUtil;
  }

  public HttpCookie createJWTCookie(UserDetails userDetails) {
    String jwt = tokenUtil.generateToken(userDetails);

    log.info("An attempt to create a JWT cookie");
    return ResponseCookie.from(JWT_COOKIE_NAME, jwt)
                         .maxAge(JWT_TOKEN_VALIDITY)
                         .httpOnly(true)
                         .build();
  }
}