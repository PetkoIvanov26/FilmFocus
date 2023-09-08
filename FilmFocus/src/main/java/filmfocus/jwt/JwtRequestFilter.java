package filmfocus.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

import static filmfocus.utils.constants.JwtConstants.JWT_COOKIE_NAME;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  private final Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);

  private final JwtTokenUtil tokenUtil;
  private final JwtUserDetailsService userDetailsService;

  @Autowired
  public JwtRequestFilter(JwtTokenUtil tokenUtil, JwtUserDetailsService userDetailsService) {
    this.tokenUtil = tokenUtil;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(
    HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws
    ServletException, IOException {
    String token = getJwtToken(request.getCookies());
    String username = null;

    if (token != null && !token.isEmpty()) {
      log.info("JWT found");
      username = tokenUtil.getUsernameFromToken(token);
    }

    if (SecurityContextHolder.getContext().getAuthentication() == null && username != null) {
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);

      if (tokenUtil.validateToken(token, userDetails)) {
        UsernamePasswordAuthenticationToken authenticationToken =
          new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        authenticationToken.setDetails(request);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        log.info("Authentication set");
      }
    }
    chain.doFilter(request, response);
  }

  private String getJwtToken(Cookie[] cookies) {
    log.info("An attempt to retrieve a JWT cookie from all cookies");
    if (cookies != null) {
      return Arrays.stream(cookies)
                   .filter(cookie -> JWT_COOKIE_NAME.equals(cookie.getName()))
                   .findFirst()
                   .map(Cookie::getValue)
                   .orElse(null);
    }
    return null;
  }
}