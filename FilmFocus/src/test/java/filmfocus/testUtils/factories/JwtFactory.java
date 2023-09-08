package filmfocus.testUtils.factories;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.Cookie;
import java.util.Collection;
import java.util.Collections;

import static filmfocus.testUtils.constants.JwtConstants.EXPIRATION;
import static filmfocus.testUtils.constants.JwtConstants.JWT_COOKIE_NAME;
import static filmfocus.testUtils.constants.JwtConstants.JWT_PASSWORD;
import static filmfocus.testUtils.constants.JwtConstants.JWT_USERNAME;
import static filmfocus.testUtils.constants.JwtConstants.NOW;
import static filmfocus.testUtils.constants.JwtConstants.USER_ROLE;
import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class JwtFactory {

  private static String secret;

  private JwtFactory() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }

  public static void setSecret(String secret) {
    JwtFactory.secret = secret;
  }

  public static String getDefaultJwtToken() {

    return Jwts.builder()
               .setSubject(JWT_USERNAME)
               .setIssuedAt(NOW)
               .setExpiration(EXPIRATION)
               .signWith(SignatureAlgorithm.HS256, secret)
               .compact();
  }

  public static HttpCookie getDefaultHttpCookie() {
    return new HttpCookie(JWT_COOKIE_NAME, JwtFactory.getDefaultJwtToken());
  }

  public static Cookie getDefaultCookie() {
    return new Cookie(JWT_COOKIE_NAME, JwtFactory.getDefaultJwtToken());
  }

  public static Cookie[] getDefaultCookieArray() {
    return new Cookie[]{getDefaultCookie()};
  }

  public static UserDetails getDefaultUserDetails() {
    return new UserDetails() {
      @Override
      public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(USER_ROLE));
      }

      @Override
      public String getPassword() {
        return JWT_PASSWORD;
      }

      @Override
      public String getUsername() {
        return JWT_USERNAME;
      }

      @Override
      public boolean isAccountNonExpired() {
        return false;
      }

      @Override
      public boolean isAccountNonLocked() {
        return false;
      }

      @Override
      public boolean isCredentialsNonExpired() {
        return false;
      }

      @Override
      public boolean isEnabled() {
        return true;
      }
    };
  }

  public static Authentication getDefaultAuthentication() {
    return new Authentication() {
      @Override
      public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
      }

      @Override
      public Object getCredentials() {
        return null;
      }

      @Override
      public Object getDetails() {
        return null;
      }

      @Override
      public Object getPrincipal() {
        return null;
      }

      @Override
      public boolean isAuthenticated() {
        return false;
      }

      @Override
      public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

      }

      @Override
      public String getName() {
        return JWT_USERNAME;
      }
    };
  }
}