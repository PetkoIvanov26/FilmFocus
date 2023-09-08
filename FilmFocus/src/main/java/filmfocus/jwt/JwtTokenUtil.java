package filmfocus.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static filmfocus.utils.constants.JwtConstants.JWT_TOKEN_VALIDITY;

@Component
public class JwtTokenUtil implements Serializable {

  private final Logger log = LoggerFactory.getLogger(JwtTokenUtil.class);
  @Value("${jwt.secret}")
  private String secret;

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public String getUsernameFromToken(String token) {
    log.info("An attempt to extract the username from the JWT token");
    return getClaimFromToken(token, Claims::getSubject);
  }

  public Date getExpirationDateFromToken(String token) {
    log.info("An attempt to extract the expiration date from the JWT token");
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    log.info("An attempt to extract the claim from the JWT token");
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
    log.info("An attempt to extract all claims from the JWT token");
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }

  private boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    log.info("An attempt to check whether the JWT has expired");
    return expiration.before(new Date());
  }

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    log.info("An attempt to generate a JWT token");
    return doGenerateToken(claims, userDetails.getUsername());
  }

  private String doGenerateToken(Map<String, Object> claims, String subject) {

    return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
               .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
               .signWith(SignatureAlgorithm.HS512, secret).compact();
  }

  public boolean validateToken(String token, UserDetails userDetails) {
    final String username = getUsernameFromToken(token);
    log.info("An attempt to validate a JWT token");
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }
}