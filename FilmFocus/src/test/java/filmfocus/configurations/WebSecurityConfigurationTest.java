package filmfocus.configurations;

import filmfocus.jwt.JwtRequestFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(
  {HttpSecurity.class, CsrfConfigurer.class, ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry.class,
   SessionManagementConfigurer.class, LogoutConfigurer.class, DefaultSecurityFilterChain.class})
public class WebSecurityConfigurationTest {

  @Mock
  private JwtRequestFilter jwtRequestFilter;

  @InjectMocks
  private WebSecurityConfiguration webSecurityConfiguration;

  @Test
  public void testSecurityFilterChain() throws Exception {
    HttpSecurity httpSecurity = PowerMockito.mock(HttpSecurity.class);
    CsrfConfigurer csrfConfigurer = PowerMockito.mock(CsrfConfigurer.class);
    ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry registry =
      PowerMockito.mock(ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry.class);
    ExpressionUrlAuthorizationConfigurer.AuthorizedUrl authorizedUrl =
      PowerMockito.mock(ExpressionUrlAuthorizationConfigurer.AuthorizedUrl.class);
    SessionManagementConfigurer sessionManagementConfigurer =
      PowerMockito.mock(SessionManagementConfigurer.class);
    LogoutConfigurer logoutConfigurer = PowerMockito.mock(LogoutConfigurer.class);
    DefaultSecurityFilterChain defaultSecurityFilterChain = PowerMockito.mock(DefaultSecurityFilterChain.class);

    PowerMockito.when(httpSecurity.csrf()).thenReturn(csrfConfigurer);
    PowerMockito.when(csrfConfigurer.csrfTokenRepository(Mockito.any())).thenReturn(csrfConfigurer);
    PowerMockito.when(csrfConfigurer.and()).thenReturn(httpSecurity);
    PowerMockito.when(httpSecurity.authorizeRequests()).thenReturn(registry);
    PowerMockito.when(registry.antMatchers((String[]) any())).thenReturn(authorizedUrl);
    PowerMockito.when(authorizedUrl.permitAll()).thenReturn(registry);
    PowerMockito.when(authorizedUrl.hasAuthority(anyString())).thenReturn(registry);
    PowerMockito.when(authorizedUrl.hasAnyAuthority(anyString(), anyString(), anyString())).thenReturn(registry);
    PowerMockito.when(authorizedUrl.hasAnyAuthority(anyString())).thenReturn(registry);
    PowerMockito.when(authorizedUrl.hasAnyAuthority(anyString(), anyString())).thenReturn(registry);
    PowerMockito.when(registry.antMatchers(any(), any())).thenReturn(authorizedUrl);
    PowerMockito.when(registry.regexMatchers(any(), any())).thenReturn(authorizedUrl);
    PowerMockito.when(registry.regexMatchers(any())).thenReturn(authorizedUrl);
    PowerMockito.when(registry.anyRequest()).thenReturn(authorizedUrl);
    PowerMockito.when(authorizedUrl.authenticated()).thenReturn(registry);
    PowerMockito.when(registry.and()).thenReturn(httpSecurity);
    PowerMockito.when(httpSecurity.sessionManagement()).thenReturn(sessionManagementConfigurer);
    PowerMockito.when(sessionManagementConfigurer.sessionCreationPolicy(Mockito.any()))
                .thenReturn(sessionManagementConfigurer);
    PowerMockito.when(sessionManagementConfigurer.and()).thenReturn(httpSecurity);
    PowerMockito.when(httpSecurity.addFilterBefore(Mockito.any(), Mockito.any())).thenReturn(httpSecurity);
    PowerMockito.when(httpSecurity.logout()).thenReturn(logoutConfigurer);
    PowerMockito.when(logoutConfigurer.logoutUrl(anyString())).thenReturn(logoutConfigurer);
    PowerMockito.when(logoutConfigurer.deleteCookies(anyString())).thenReturn(logoutConfigurer);
    PowerMockito.when(logoutConfigurer.logoutSuccessHandler(Mockito.any())).thenReturn(logoutConfigurer);
    PowerMockito.when(httpSecurity.build()).thenReturn(defaultSecurityFilterChain);

    SecurityFilterChain result = webSecurityConfiguration.securityFilterChain(httpSecurity);

    assertNotNull(result);
  }

  @Test
  public void testPasswordEncoder() {
    PasswordEncoder passwordEncoder = webSecurityConfiguration.passwordEncoder();

    assertNotNull(passwordEncoder);
  }

  @Test
  public void testAuthenticationManager() throws Exception {
    AuthenticationConfiguration authenticationConfiguration = mock(AuthenticationConfiguration.class);
    AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    when(authenticationConfiguration.getAuthenticationManager()).thenReturn(authenticationManager);

    AuthenticationManager manager = webSecurityConfiguration.authenticationManager(authenticationConfiguration);

    assertEquals(authenticationManager, manager);
  }
}