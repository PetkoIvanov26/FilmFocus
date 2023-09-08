package filmfocus.configurations;

import filmfocus.jwt.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.servlet.http.HttpServletResponse;

import static filmfocus.utils.constants.JwtConstants.JWT_COOKIE_NAME;
import static filmfocus.utils.constants.RoleConstants.DEFAULT_ADMIN_ROLE;
import static filmfocus.utils.constants.RoleConstants.DEFAULT_USER_ROLE;
import static filmfocus.utils.constants.RoleConstants.DEFAULT_VENDOR_ROLE;

@EnableWebSecurity
public class WebSecurityConfiguration {

  private static final String[] AUTH_PATH = {
    "/login",
    "/registration",
    "/logout",
    "/password-recovery"
  };

  private static final String[] GUEST_GET_LIST = {
    "/categories.*",
    "/cinemas.*",
    "/cinemas/\\d/halls",
    "/cinemas/\\d/reviews",
    "/items.*",
    "/movies.*",
    "/categories/\\d/movies",
    "/programs.*",
    "/cinemas/\\d/programs",
    "/programs/\\d/projections",
    "/movies/\\d/projections",
    "/movies/\\d/reviews",
    "/projections(\\?.*|\\z)"
  };

  private static final String[] USER_LIST = {
    "/reviews/\\d.*",
    "/cinemas/\\d/reviews",
    "/movies/\\d/reviews",
    "/users/\\d/orders",
    "/users\\?username=.*",
    "/users\\?email=.*",
    "/users/\\d.*"
  };

  private static final String[] VENDOR_LIST = {
    "/items.*",
    "/items/\\d.*",
    "/discounts.*",
    "/discounts/\\d.*",
    "/orders.*",
    "/orders/\\d.*",
    "/tickets.*",
    "/projections/\\d/tickets.*"
  };

  private static final String[] ADMIN_LIST = {
    "/categories/\\d.*",
    "/cinemas/\\d.*",
    "/halls.*",
    "/halls/\\d.*",
    "/movies/\\d.*",
    "/programs/\\d.*",
    "/projections/\\d.*",
    "/reports.*",
    "/roles.*",
    "/roles/\\d.*",
    "/users\\?roleName=.*",
    "/users\\?joinDate=.*",
    "/admins.*",
    "/admins/\\d.*"
  };

  private static final String LOGOUT_URL = "/logout";

  private final JwtRequestFilter jwtRequestFilter;

  @Autowired
  public WebSecurityConfiguration(JwtRequestFilter jwtRequestFilter) {
    this.jwtRequestFilter = jwtRequestFilter;
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
      .csrf()
      .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
      .and()
      .authorizeRequests()
      .antMatchers(AUTH_PATH).permitAll()
      .regexMatchers(HttpMethod.GET, GUEST_GET_LIST).permitAll()
      .regexMatchers(USER_LIST).hasAnyAuthority(DEFAULT_USER_ROLE, DEFAULT_VENDOR_ROLE, DEFAULT_ADMIN_ROLE)
      .regexMatchers(VENDOR_LIST).hasAnyAuthority(DEFAULT_VENDOR_ROLE, DEFAULT_ADMIN_ROLE)
      .regexMatchers(GUEST_GET_LIST).hasAnyAuthority(DEFAULT_ADMIN_ROLE)
      .regexMatchers(ADMIN_LIST).hasAuthority(DEFAULT_ADMIN_ROLE)
      .anyRequest()
      .authenticated()
      .and()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
      .logout()
      .logoutUrl(LOGOUT_URL)
      .deleteCookies(JWT_COOKIE_NAME)
      .logoutSuccessHandler((request, response, authentication) -> response.setStatus(HttpServletResponse.SC_OK));
    return httpSecurity.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws
    Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
}