package filmfocus.jwt;

import filmfocus.exceptions.UserNotFoundException;
import filmfocus.repositories.UserRepository;
import filmfocus.testUtils.factories.UserFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static filmfocus.testUtils.constants.UserConstants.PASSWORD;
import static filmfocus.testUtils.constants.UserConstants.USERNAME;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JwtUserDetailsServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private JwtUserDetailsService jwtUserDetailsService;

  @Test
  public void testLoadUserByUsername_userFound_success() {
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(UserFactory.getDefaultUser()));

    UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(USERNAME);

    assertEquals(USERNAME, userDetails.getUsername());
    assertEquals(PASSWORD, userDetails.getPassword());
    assertEquals(1, userDetails.getAuthorities().size());
  }

  @Test(expected = UserNotFoundException.class)
  public void testLoadUserByUsername_userNotFound_throwsUserNotFoundException() {
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());

    jwtUserDetailsService.loadUserByUsername(USERNAME);
  }
}