package filmfocus.services;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import filmfocus.exceptions.NotAuthorizedException;
import filmfocus.exceptions.NotLoggedInException;
import filmfocus.exceptions.RoleNotChosenException;
import filmfocus.exceptions.UserEmailAlreadyExistsException;
import filmfocus.exceptions.UserNotFoundException;
import filmfocus.exceptions.UsernameAlreadyExistsException;
import filmfocus.jwt.JwtCookieUtil;
import filmfocus.mappers.UserMapper;
import filmfocus.models.dtos.UserDto;
import filmfocus.models.entities.Role;
import filmfocus.models.entities.User;
import filmfocus.models.requests.LoginRequest;
import filmfocus.repositories.UserRepository;
import filmfocus.testUtils.factories.JwtFactory;
import filmfocus.testUtils.factories.RoleFactory;
import filmfocus.testUtils.factories.UserFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static filmfocus.testUtils.constants.RoleConstants.NAME;
import static filmfocus.testUtils.constants.UserConstants.EMAIL;
import static filmfocus.testUtils.constants.UserConstants.ID;
import static filmfocus.testUtils.constants.UserConstants.JOIN_DATE;
import static filmfocus.testUtils.constants.UserConstants.PASSWORD;
import static filmfocus.testUtils.constants.UserConstants.USERNAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations = "classpath:application.yml")
public class UserServiceTest {

  private static final int INVALID_ID = 2;

  @Value("${jwt.secret}")
  private String secret;

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private BCryptPasswordEncoder passwordEncoder;

  @Mock
  private EmailService emailService;

  @Mock
  private JwtCookieUtil jwtCookieUtil;

  @Mock
  private UserRepository userRepository;

  @Mock
  private Random random;

  @Mock
  private RoleService roleService;

  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private UserService userService;

  @Before
  public void setUp() {
    JwtFactory.setSecret(secret);
  }

  @Test
  public void testLogin_cookieObtained_success() {
    UserDetails userDetails = mock(UserDetails.class);

    Authentication authentication = mock(Authentication.class);

    HttpCookie httpCookie = mock(HttpCookie.class);

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(jwtCookieUtil.createJWTCookie(userDetails)).thenReturn(httpCookie);

    HttpCookie resultCookie = userService.login(new LoginRequest(USERNAME, PASSWORD));
    assertEquals(resultCookie, httpCookie);
  }

  @Test
  public void testRegisterUser_userRegistered_cookieObtained() throws MailjetSocketTimeoutException, MailjetException {
    HttpCookie httpCookie = JwtFactory.getDefaultHttpCookie();

    when(passwordEncoder.encode(anyString())).thenReturn(PASSWORD);
    when(userRepository.save(any())).thenReturn(UserFactory.getDefaultUser());
    when(authenticationManager.authenticate(any())).thenReturn(JwtFactory.getDefaultAuthentication());
    when(jwtCookieUtil.createJWTCookie(any())).thenReturn(httpCookie);

    HttpCookie resultCookie = userService.registerUser(UserFactory.getDefaultUserRequest());

    assertEquals(resultCookie, httpCookie);
  }

  @Test
  public void testRegisterUserByAdmin_userRegistered_cookieObtained() {
    HttpCookie httpCookie = JwtFactory.getDefaultHttpCookie();

    when(passwordEncoder.encode(anyString())).thenReturn(PASSWORD);
    when(userRepository.save(any())).thenReturn(UserFactory.getDefaultUser());
    when(authenticationManager.authenticate(any())).thenReturn(JwtFactory.getDefaultAuthentication());
    when(jwtCookieUtil.createJWTCookie(any())).thenReturn(httpCookie);

    HttpCookie resultCookie = userService.registerUserByAdmin(UserFactory.getDefaultAdminRequest());

    assertEquals(resultCookie, httpCookie);
  }

  @Test(expected = UsernameAlreadyExistsException.class)
  public void testAddUser_usernameExists_throwsUserEmailAlreadyExistsException() throws MailjetSocketTimeoutException,
    MailjetException {
    when(passwordEncoder.encode(anyString())).thenReturn(PASSWORD);
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(new User()));

    userService.addUser(UserFactory.getDefaultUserRequest());
  }

  @Test(expected = UserEmailAlreadyExistsException.class)
  public void testAddUser_emailExists_throwsUserEmailAlreadyExistsException() throws MailjetSocketTimeoutException,
    MailjetException {
    when(passwordEncoder.encode(anyString())).thenReturn(PASSWORD);
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());
    when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(new User()));

    userService.addUser(UserFactory.getDefaultUserRequest());
  }

  @Test
  public void testGetRoleByName_success() {
    when(roleService.getRoleByName(anyString())).thenReturn(RoleFactory.getDefaultRole());

    Role result = userService.getRoleByName(NAME);

    assertEquals(RoleFactory.getDefaultRole(), result);
  }

  @Test
  public void testGetDefaultRoleList_success() {
    List<Role> expected = RoleFactory.getDefaultRoleList();

    when(roleService.getRoleByName(NAME)).thenReturn(RoleFactory.getDefaultRole());

    List<Role> roles = userService.getDefaultRoleList();

    assertEquals(expected, roles);
  }

  @Test
  public void testGetUserRoles_success() {
    Role role1 = new Role(1, "ADMIN");
    Role role2 = new Role(2, "USER");

    when(roleService.getRoleByName("ADMIN")).thenReturn(role1);
    when(roleService.getRoleByName("USER")).thenReturn(role2);

    List<String> roleNames = Arrays.asList("ADMIN", "USER");

    List<Role> roles = userService.getUserRoles(roleNames);

    assertEquals(2, roles.size());
    assertEquals(role1, roles.get(0));
    assertEquals(role2, roles.get(1));
  }

  @Test(expected = RoleNotChosenException.class)
  public void testGetUserRoles_rolesEmpty_throwsRoleNotChosenException() {
    userService.getUserRoles(Collections.emptyList());
  }

  @Test
  public void testGetUserById_success() {
    User expected = UserFactory.getDefaultUser();

    when(userRepository.findById(anyInt())).thenReturn(Optional.of(expected));

    User user = userService.getUserById(ID);

    assertEquals(expected, user);
  }

  @Test
  public void testGetUserById_userNotFound() {
    when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userService.getUserById(ID));
  }

  @Test
  public void testGetUserDtoById_success() {
    UserDto expected = UserFactory.getDefaultUserDto();

    when(userRepository.findById(anyInt())).thenReturn(Optional.of(UserFactory.getDefaultUser()));
    when(userMapper.mapUserToUserDto(UserFactory.getDefaultUser())).thenReturn(expected);

    UserDto userDto = userService.getUserDtoById(ID);

    assertEquals(expected, userDto);
  }

  @Test
  public void testGetUserByUsername_success() {
    User expected = UserFactory.getDefaultUser();

    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(expected));

    User user = userService.getUserByUsername(USERNAME);

    assertEquals(expected, user);
  }

  @Test
  public void testGetUserByUsername_userNotFound() {
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername(USERNAME));
  }

  @Test
  public void testGetUserDtoByUsername_success() {
    UserDto expected = UserFactory.getDefaultUserDto();

    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(UserFactory.getDefaultUser()));
    when(userMapper.mapUserToUserDto(UserFactory.getDefaultUser())).thenReturn(expected);

    UserDto user = userService.getUserDtoByUsername(USERNAME);

    assertEquals(expected, user);
  }

  @Test
  public void testGetUserByEmail_success() {
    User expected = UserFactory.getDefaultUser();

    when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(expected));

    User user = userService.getUserByEmail(EMAIL);

    assertEquals(expected, user);
  }

  @Test
  public void testGetUserByEmail_userNotFound() {
    when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail(EMAIL));
  }

  @Test
  public void testGetUserDtoByEmail_success() {
    UserDto expected = UserFactory.getDefaultUserDto();

    when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(UserFactory.getDefaultUser()));
    when(userMapper.mapUserToUserDto(UserFactory.getDefaultUser())).thenReturn(expected);

    UserDto result = userService.getUserDtoByEmail(EMAIL);

    assertEquals(expected, result);
  }

  @Test
  public void testGetUsersByRoleName_success() {
    List<User> expected = UserFactory.getDefaultUserList();

    when(userRepository.findAllByRolesName(NAME)).thenReturn(expected);

    List<User> result = userService.getUsersByRoleName(NAME);

    assertEquals(expected, result);
  }

  @Test
  public void testGetUsersDtoByRoleName_success() {
    List<UserDto> expected = UserFactory.getDefaultUserDtoList();

    when(userService.getUsersByRoleName(NAME)).thenReturn(UserFactory.getDefaultUserList());
    when(userMapper.mapUsersToUserDtos(UserFactory.getDefaultUserList())).thenReturn(expected);

    List<UserDto> resultList = userService.getUsersDtoByRoleName(NAME);

    assertEquals(expected, resultList);
  }

  @Test
  public void testGetUsersByJoinDate_before_success() {
    User user2 = UserFactory.getDefaultUser();
    user2.setId(2);
    List<User> expectedUsers = Arrays.asList(UserFactory.getDefaultUser(), user2);

    when(userRepository.findAllByJoinDateBefore(any(LocalDate.class))).thenReturn(expectedUsers);

    List<User> users = userService.getUsersByJoinDate(JOIN_DATE, true);

    assertEquals(expectedUsers, users);
  }

  @Test
  public void testGetUsersByJoinDate_after_success() {
    User user2 = UserFactory.getDefaultUser();
    user2.setId(2);
    List<User> expectedUsers = Arrays.asList(UserFactory.getDefaultUser(), user2);

    when(userRepository.findAllByJoinDateAfter(any(LocalDate.class))).thenReturn(expectedUsers);

    List<User> users = userService.getUsersByJoinDate(JOIN_DATE, false);

    assertEquals(expectedUsers, users);
  }

  @Test
  public void getUsersDtosByJoinDate() {
    User user2 = UserFactory.getDefaultUser();
    user2.setId(2);

    List<User> users = Arrays.asList(UserFactory.getDefaultUser(), user2);

    when(userService.getUsersByJoinDate(JOIN_DATE, true)).thenReturn(users);
    when(userService.getUsersByJoinDate(JOIN_DATE, true)).thenReturn(UserFactory.getDefaultUserList());
    when(userMapper.mapUsersToUserDtos(UserFactory.getDefaultUserList())).thenReturn(
      UserFactory.getDefaultUserDtoList());

    List<UserDto> actualUserDtos = userService.getUsersDtosByJoinDate(JOIN_DATE, true);
    assertEquals(UserFactory.getDefaultUserDtoList(), actualUserDtos);
  }

  @Test
  public void testUpdateUserByAdmin_userUpdated_success() {
    UserDto expected = UserFactory.getDefaultUserDto();

    when(userRepository.findById(anyInt())).thenReturn(Optional.of(UserFactory.getDefaultUser()));
    when(userMapper.mapUserToUserDto(any())).thenReturn(expected);
    when(roleService.getRoleByName(anyString())).thenReturn(RoleFactory.getDefaultRole());
    when(userRepository.save(any())).thenReturn(UserFactory.getDefaultUser());

    UserDto result = userService.updateUserByAdmin(UserFactory.getDefaultAdminRequest(), ID);

    assertEquals(expected, result);
  }

  @Test
  public void testUpdateUser_userUpdated_success() {
    User expectedUser = UserFactory.getDefaultUser();
    UserDto expectedUserDto = UserFactory.getDefaultUserDto();
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Authentication authentication = JwtFactory.getDefaultAuthentication();
    SecurityContextHolder.setContext(securityContext);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(userMapper.mapUserToUserDto(any())).thenReturn(expectedUserDto);
    when(passwordEncoder.encode(any())).thenReturn(PASSWORD);
    when(userRepository.findById(anyInt())).thenReturn(Optional.of(expectedUser));
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(expectedUser))
                                                        .thenReturn(Optional.empty());
    when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
    when(roleService.getRoleByName(anyString())).thenReturn(RoleFactory.getDefaultRole());

    UserDto resultUser = userService.updateUser(UserFactory.getDefaultUserRequest(), ID);

    assertEquals(expectedUserDto, resultUser);
  }

  @Test(expected = NotAuthorizedException.class)
  public void testUpdateUser_userNotAuthorized_throwsNotAuthorizedException() {
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    SecurityContextHolder.setContext(securityContext);
    Authentication authentication = JwtFactory.getDefaultAuthentication();

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(userRepository.findById(anyInt())).thenReturn(Optional.of(UserFactory.getDefaultUser()));
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(UserFactory.getDefaultUser()));
    when(roleService.getRoleByName(anyString())).thenReturn(RoleFactory.getDefaultAdminRole());

    userService.updateUser(UserFactory.getDefaultUserRequest(), INVALID_ID);
  }

  @Test
  public void testDeleteUser_userDeleted_success() {
    UserDto userDto = UserFactory.getDefaultUserDto();
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    SecurityContextHolder.setContext(securityContext);
    Authentication authentication = JwtFactory.getDefaultAuthentication();

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(userMapper.mapUserToUserDto(any())).thenReturn(userDto);
    when(userRepository.findById(anyInt())).thenReturn(Optional.of(UserFactory.getDefaultUser()));
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(UserFactory.getDefaultUser()));
    when(roleService.getRoleByName(anyString())).thenReturn(RoleFactory.getDefaultRole());

    UserDto result = userService.deleteUser(ID);

    assertEquals(userDto, result);
  }

  @Test(expected = NotAuthorizedException.class)
  public void testDeleteUser_userNotAuthorized_throwsNotAuthorizedException() {
    UserDto userDto = UserFactory.getDefaultUserDto();
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    SecurityContextHolder.setContext(securityContext);
    Authentication authentication = JwtFactory.getDefaultAuthentication();

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(userMapper.mapUserToUserDto(any())).thenReturn(userDto);
    when(userRepository.findById(anyInt())).thenReturn(Optional.of(UserFactory.getDefaultUser()));
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(UserFactory.getDefaultUser()));
    when(roleService.getRoleByName(anyString())).thenReturn(RoleFactory.getDefaultAdminRole());

    userService.deleteUser(INVALID_ID);
  }

  @Test
  public void testGetCurrentUser_userLoggedIn_success() {
    User expected = UserFactory.getDefaultUser();
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    SecurityContextHolder.setContext(securityContext);
    Authentication authentication = JwtFactory.getDefaultAuthentication();

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(expected));

    User result = userService.getCurrentUser();

    assertEquals(expected, result);
  }

  @Test(expected = UserNotFoundException.class)
  public void testGetUserByUsernameOnLogin_userNotFound_throwsUserNotFoundException() {
    UserDto userDto = UserFactory.getDefaultUserDto();
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    SecurityContextHolder.setContext(securityContext);
    Authentication authentication = JwtFactory.getDefaultAuthentication();

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(userMapper.mapUserToUserDto(any())).thenReturn(userDto);
    when(userRepository.findById(anyInt())).thenReturn(Optional.of(UserFactory.getDefaultUser()));
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());

    UserDto result = userService.deleteUser(ID);

    assertEquals(userDto, result);
  }

  @Test(expected = NotLoggedInException.class)
  public void testIsAuthorized_userNotLogged_throwsNotLoggedInException() {
    UserDto userDto = UserFactory.getDefaultUserDto();
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    SecurityContextHolder.setContext(securityContext);
    Authentication authentication = Mockito.mock(Authentication.class);

    when(authentication.getName()).thenReturn(null);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(userRepository.findById(anyInt())).thenReturn(Optional.of(UserFactory.getDefaultUser()));

    UserDto result = userService.updateUser(UserFactory.getDefaultUserRequest(), ID);

    assertEquals(userDto, result);
  }

  @Test
  public void testIsAuthorized_roleAdmin_returnsTrue() {
    User user = UserFactory.getDefaultUser();
    user.setRoles(Collections.singletonList(RoleFactory.getDefaultAdminRole()));
    UserDto userDto = UserFactory.getDefaultUserDto();
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    SecurityContextHolder.setContext(securityContext);
    Authentication authentication = JwtFactory.getDefaultAuthentication();

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(userMapper.mapUserToUserDto(any())).thenReturn(userDto);
    when(userRepository.findById(anyInt())).thenReturn(Optional.of(UserFactory.getDefaultUser()));
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user)).thenReturn(Optional.empty());
    when(roleService.getRoleByName(anyString())).thenReturn(RoleFactory.getDefaultAdminRole());

    UserDto result = userService.updateUser(UserFactory.getDefaultUserRequest(), ID);

    assertEquals(userDto, result);
  }

  @Test
  public void testIsCurrentUserAdmin_roleAdmin_returnsTrue() {
    User expected = UserFactory.getDefaultAdmin();
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    SecurityContextHolder.setContext(securityContext);
    Authentication authentication = JwtFactory.getDefaultAuthentication();

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(expected));

    boolean result = userService.isCurrentUserAdmin();

    assertTrue(result);
  }

  @Test
  public void testIsCurrentUserAuthorized_userIsAdmin_returnsTrue() {
    User expected = UserFactory.getDefaultAdmin();
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    SecurityContextHolder.setContext(securityContext);
    Authentication authentication = JwtFactory.getDefaultAuthentication();

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(expected));

    boolean result = userService.isCurrentUserAuthorized(ID);

    assertTrue(result);
  }

  @Test
  public void testIsCurrentUserAuthorized_userAuthorized_returnsTrue() {
    User expected = UserFactory.getDefaultUser();
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    SecurityContextHolder.setContext(securityContext);
    Authentication authentication = JwtFactory.getDefaultAuthentication();

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(expected));

    boolean result = userService.isCurrentUserAuthorized(ID);

    assertTrue(result);
  }

  @Test
  public void testRecoverPassword() throws MailjetSocketTimeoutException, MailjetException {
    User expectedUser = UserFactory.getDefaultUser();
    when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(expectedUser));
    when(random.nextInt(anyInt())).thenReturn(1);
    when(passwordEncoder.encode(any())).thenReturn(PASSWORD);
    when(userRepository.save(any())).thenReturn(expectedUser);
    Mockito.doNothing().when(emailService).sendPasswordConfirmationEmail(any(), anyString());

    User resultUser = userService.recoverPassword(USERNAME);

    assertEquals(expectedUser, resultUser);
  }
}