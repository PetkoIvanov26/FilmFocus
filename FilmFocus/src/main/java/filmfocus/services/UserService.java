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
import filmfocus.models.requests.AdminRequest;
import filmfocus.models.requests.LoginRequest;
import filmfocus.models.requests.UserRequest;
import filmfocus.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import static filmfocus.utils.constants.ExceptionMessages.NOT_AUTHORIZED_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.NOT_LOGGED_IN_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.ROLE_NOT_CHOSEN_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.USERNAME_ALREADY_EXISTS_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.USER_EMAIL_ALREADY_EXISTS_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.USER_NOT_FOUND_MESSAGE;
import static filmfocus.utils.constants.RoleConstants.DEFAULT_ADMIN_ROLE;
import static filmfocus.utils.constants.RoleConstants.DEFAULT_USER_ROLE;
import static filmfocus.utils.constants.UserConstants.DEFAULT_GUEST_USERNAME;

@Service
public class UserService {

  private static final Logger log = LoggerFactory.getLogger(UserService.class);

  private final AuthenticationManager authenticationManager;
  private final BCryptPasswordEncoder passwordEncoder;
  private final EmailService emailService;
  private final JwtCookieUtil jwtCookieUtil;
  private final UserRepository userRepository;
  private final Random random;
  private final RoleService roleService;
  private final UserMapper userMapper;

  @Autowired
  public UserService(
    AuthenticationManager authenticationManager, BCryptPasswordEncoder passwordEncoder, EmailService emailService,
    JwtCookieUtil jwtCookieUtil,
    UserRepository userRepository, Random random, RoleService roleService,
    UserMapper userMapper) {
    this.authenticationManager = authenticationManager;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
    this.jwtCookieUtil = jwtCookieUtil;
    this.userRepository = userRepository;
    this.random = random;
    this.roleService = roleService;
    this.userMapper = userMapper;
  }

  public HttpCookie login(LoginRequest request) {
    UserDetails userDetails = (UserDetails) authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())).getPrincipal();

    log.info("A login attempt");

    return jwtCookieUtil.createJWTCookie(userDetails);
  }

  public HttpCookie registerUser(UserRequest userRequest) throws MailjetSocketTimeoutException, MailjetException {
    addUser(userRequest);

    log.info("An attempt a new user to be registered");

    return login(new LoginRequest(userRequest.getUsername(), userRequest.getPassword()));
  }

  public HttpCookie registerUserByAdmin(AdminRequest request) {
    addUserByAdmin(request);

    return login(new LoginRequest(request.getUsername(), request.getPassword()));
  }

  public User addUser(UserRequest userRequest) throws MailjetSocketTimeoutException, MailjetException {
    String password = passwordEncoder.encode(userRequest.getPassword());

    userValidation(userRequest);

    User user =
      new User(userRequest.getUsername(), password, userRequest.getEmail(), userRequest.getFirstName(),
               userRequest.getLastName(),
               LocalDate.now(), getDefaultRoleList());

    emailService.sendRegistrationConfirmationEmail(user);

    log.info("Trying to add a new user");


    return userRepository.save(user);
  }

  public User addUserByAdmin(AdminRequest request) {
    String password = passwordEncoder.encode(request.getPassword());

    User user =
      new User(request.getUsername(), password, request.getEmail(), request.getFirstName(), request.getLastName(),
               LocalDate.now(), getUserRoles(request.getRoleNames()));

    log.info("An attempt a new user to be added by an admin");

    return userRepository.save(user);
  }

  public Role getRoleByName(String roleName) {
    log.info(String.format("Trying to retrieve the user role %s", roleName));

    return roleService.getRoleByName(roleName);
  }

  public List<Role> getDefaultRoleList() {
    log.info("Trying to retrieve the default user role");

    return Collections.singletonList(getRoleByName(DEFAULT_USER_ROLE));
  }

  public List<Role> getUserRoles(List<String> roleNames) {
    if (Objects.nonNull(roleNames) && roleNames.size() > 0) {

      log.info("Trying to map all ids to user roles");

      return roleNames.stream()
                      .map(this::getRoleByName)
                      .collect(Collectors.toList());
    } else {
      throw new RoleNotChosenException(ROLE_NOT_CHOSEN_MESSAGE);
    }
  }

  public User getUserById(int id) {
    log.info(String.format("Trying to retrieve user with id %d", id));

    return userRepository.findById(id).orElseThrow(() -> {
      log.error(String.format("Exception caught: %s", USER_NOT_FOUND_MESSAGE));

      throw new UserNotFoundException(USER_NOT_FOUND_MESSAGE);
    });
  }

  public UserDto getUserDtoById(int id) {
    log.info(String.format("Trying to retrieve user DTO with id %d", id));

    return userMapper.mapUserToUserDto(getUserById(id));
  }

  public User getUserByUsername(String username) {
    log.info(String.format("Trying to retrieve user with username %s", username));

    return userRepository.findUserByUsername(username).orElseThrow(() -> {
      log.error(String.format("Exception caught: %s", USER_NOT_FOUND_MESSAGE));

      throw new UserNotFoundException(USER_NOT_FOUND_MESSAGE);
    });
  }

  public UserDto getUserDtoByUsername(String username) {
    log.info(String.format("Trying to retrieve user DTO with username %s", username));

    return userMapper.mapUserToUserDto(getUserByUsername(username));
  }

  public User getUserByEmail(String email) {
    log.info(String.format("Trying to retrieve user with email %s", email));

    return userRepository.findUserByEmail(email).orElseThrow(() -> {
      log.error(String.format("Exception caught: %s", USER_NOT_FOUND_MESSAGE));

      throw new UserNotFoundException(USER_NOT_FOUND_MESSAGE);
    });
  }

  public UserDto getUserDtoByEmail(String email) {
    log.info(String.format("Trying to retrieve user DTO with email %s", email));

    return userMapper.mapUserToUserDto(getUserByEmail(email));
  }

  public List<User> getUsersByRoleName(String roleName) {
    log.info(String.format("Trying to retrieve users with role %s", roleName));

    return userRepository.findAllByRolesName(roleName.toUpperCase());
  }

  public List<UserDto> getUsersDtoByRoleName(String roleName) {
    log.info(String.format("Trying to retrieve user DTOS with role %s", roleName));

    return userMapper.mapUsersToUserDtos(getUsersByRoleName(roleName));
  }

  public List<User> getUsersByJoinDate(LocalDate joinDate, boolean isBefore) {
    return isBefore ? userRepository.findAllByJoinDateBefore(joinDate)
                    : userRepository.findAllByJoinDateAfter(joinDate);
  }

  public List<UserDto> getUsersDtosByJoinDate(LocalDate joinDate, boolean isBefore) {
    return userMapper.mapUsersToUserDtos(getUsersByJoinDate(joinDate, isBefore));
  }

  private User getUserByUsernameOnLogin(String username) {
    return userRepository.findUserByUsername(username).orElseThrow(() -> {
      log.error(String.format("Exception caught: %s", USER_NOT_FOUND_MESSAGE));

      throw new UserNotFoundException(USER_NOT_FOUND_MESSAGE);
    });
  }

  public User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUsername = authentication.getName();

    return getUserByUsername(currentUsername);
  }

  public UserDto updateUserByAdmin(AdminRequest request, int id) {
    UserDto userDto = getUserDtoById(id);
    String password = passwordEncoder.encode(request.getPassword());
    List<Role> roles = getUserRoles(request.getRoleNames());

    userRepository.save(new User(id, request.getUsername(), password, request.getEmail(),
                                 request.getFirstName(), request.getLastName(), userDto.getJoinDate(), roles));

    log.info(String.format("User with id %d was updated by an admin", id));
    return userDto;
  }

  public UserDto updateUser(UserRequest userRequest, int id) {
    User user = getUserById(id);
    UserDto oldUser = userMapper.mapUserToUserDto(user);
    String password = passwordEncoder.encode(userRequest.getPassword());

    if (!isAuthorized(id)) {
      log.error(String.format("Exception caught: %s", NOT_AUTHORIZED_MESSAGE));

      throw new NotAuthorizedException(NOT_AUTHORIZED_MESSAGE);
    }

    userRepository.save(new User(id, userRequest.getUsername(), password, userRequest.getEmail(),
                                 userRequest.getFirstName(), userRequest.getLastName(), user.getJoinDate(),
                                 user.getRoles()));

    log.info(String.format("User with id %d was updated", id));

    return oldUser;
  }

  public UserDto deleteUser(int id) {
    UserDto userDto = getUserDtoById(id);

    if (!isAuthorized(id)) {
      log.error(String.format("Exception caught: %s", NOT_AUTHORIZED_MESSAGE));

      throw new NotAuthorizedException(NOT_AUTHORIZED_MESSAGE);
    }

    userRepository.deleteById(id);

    if (Objects.equals(SecurityContextHolder.getContext().getAuthentication().getName(), userDto.getUsername())) {
      SecurityContextHolder.clearContext();
    }

    log.info(String.format("User with id %d was deleted", id));

    return userDto;
  }

  public boolean isCurrentUserAdmin() {
    User currentUser = getCurrentUser();

    return currentUser.getRoles().stream()
                      .anyMatch(role -> Objects.equals(DEFAULT_ADMIN_ROLE, role.getName()));
  }

  public boolean isCurrentUserAuthorized(int userId) {
    User currentUser = getCurrentUser();

    if (isCurrentUserAdmin()) {
      return true;
    }

    return currentUser.getId() == userId;
  }

  public User recoverPassword(String username) throws MailjetSocketTimeoutException, MailjetException {
    User user = getUserByUsername(username);
    String newPassword = generateRandomPassword();

    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
    emailService.sendPasswordConfirmationEmail(user, newPassword);

    log.info(String.format("The user %s password has been changed", username));
    return user;
  }

  private boolean isAuthorized(int userId) {
    String currentUsername = null;

    if (Objects.nonNull(SecurityContextHolder.getContext()) &&
        Objects.nonNull(SecurityContextHolder.getContext().getAuthentication())) {
      currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    }

    if (Objects.isNull(currentUsername) || Objects.equals(DEFAULT_GUEST_USERNAME, currentUsername)) {
      log.error(String.format("Exception caught: %s", NOT_LOGGED_IN_MESSAGE));

      throw new NotLoggedInException(NOT_LOGGED_IN_MESSAGE);
    }

    User currentUser = getUserByUsernameOnLogin(currentUsername);
    Role adminRole = getRoleByName(DEFAULT_ADMIN_ROLE);

    for (Role role : currentUser.getRoles()) {
      if (Objects.equals(role, adminRole)) {
        return true;
      }
    }

    return userId == currentUser.getId();
  }

  private void userValidation(UserRequest userRequest) {
    userRepository.findUserByUsername(userRequest.getUsername()).ifPresent(user -> {
      log.error(String.format("Exception caught: %s", USERNAME_ALREADY_EXISTS_MESSAGE));

      throw new UsernameAlreadyExistsException(USERNAME_ALREADY_EXISTS_MESSAGE);
    });

    userRepository.findUserByEmail(userRequest.getEmail()).ifPresent(user -> {
      log.error(String.format("Exception caught: %s", USER_EMAIL_ALREADY_EXISTS_MESSAGE));

      throw new UserEmailAlreadyExistsException(USER_EMAIL_ALREADY_EXISTS_MESSAGE);
    });
  }

  private String generateRandomPassword() {
    StringBuilder password = new StringBuilder();

    for (int i = 0; i < 8; i++) {
      int digit = random.nextInt(10);
      password.append(digit);
    }

    return password.toString();
  }
}