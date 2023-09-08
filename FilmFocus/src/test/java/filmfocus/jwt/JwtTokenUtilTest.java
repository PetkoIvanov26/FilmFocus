package filmfocus.jwt;

import filmfocus.testUtils.factories.JwtFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static filmfocus.testUtils.constants.JwtConstants.JWT_USERNAME;
import static filmfocus.testUtils.constants.JwtConstants.NOW;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations = "classpath:application.yml")
public class JwtTokenUtilTest {

  private final JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
  @Value("${jwt.secret}")
  private String secret;

  @Before
  public void setUp() {
    JwtFactory.setSecret(secret);
    jwtTokenUtil.setSecret(secret);
  }

  @Test
  public void testGetUsernameFromToken_usernameExtracted_success() {
    String token = jwtTokenUtil.getUsernameFromToken(JwtFactory.getDefaultJwtToken());

    assertEquals(JWT_USERNAME, token);
  }

  @Test
  public void testGetExpirationDateFromToken_dateValid_returnsTrue() {
    Date date = jwtTokenUtil.getExpirationDateFromToken(JwtFactory.getDefaultJwtToken());

    assertTrue(date.after(NOW));
  }

  @Test
  public void testGenerateToken_tokenGenerated_success() {
    String token = jwtTokenUtil.generateToken(JwtFactory.getDefaultUserDetails());

    assertNotNull(token);
  }

  @Test
  public void testValidateToken_tokenValidated_success() {
    boolean result = jwtTokenUtil.validateToken(JwtFactory.getDefaultJwtToken(), JwtFactory.getDefaultUserDetails());

    assertTrue(result);
  }
}