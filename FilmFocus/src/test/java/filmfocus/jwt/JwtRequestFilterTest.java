package filmfocus.jwt;

import filmfocus.testUtils.factories.JwtFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static filmfocus.testUtils.constants.JwtConstants.JWT_USERNAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations = "classpath:application.yml")
public class JwtRequestFilterTest {

  @Value("${jwt.secret}")
  private String secret;

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private FilterChain filterChain;

  @Mock
  private JwtTokenUtil tokenUtil;

  @Mock
  private JwtUserDetailsService userDetailsService;

  @InjectMocks
  private JwtRequestFilter requestFilter;

  @Before
  public void setUp() {
    JwtFactory.setSecret(secret);
  }

  @Test
  public void testDoFilterInternal_success() throws ServletException, IOException {
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    SecurityContextHolder.setContext(securityContext);
    when(securityContext.getAuthentication()).thenReturn(null);
    when(request.getCookies()).thenReturn(JwtFactory.getDefaultCookieArray());
    when(tokenUtil.getUsernameFromToken(anyString())).thenReturn(JWT_USERNAME);
    when(userDetailsService.loadUserByUsername(anyString())).thenReturn(JwtFactory.getDefaultUserDetails());
    when(tokenUtil.validateToken(anyString(), any())).thenReturn(true);

    requestFilter.doFilterInternal(request, response, filterChain);

    verify(userDetailsService, times(1)).loadUserByUsername(JWT_USERNAME);
    verify(tokenUtil, times(1)).validateToken(any(), any());
    verify(filterChain, times(1)).doFilter(request, response);
  }

  @Test
  public void testDoFilterInternal_cookiesAreNull() throws ServletException, IOException {
    when(request.getCookies()).thenReturn(null);

    requestFilter.doFilterInternal(request, response, filterChain);

    verify(request, times(1)).getCookies();
    verify(filterChain, times(1)).doFilter(request, response);
  }
}