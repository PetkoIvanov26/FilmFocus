package filmfocus.controllers;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import filmfocus.models.dtos.UserDto;
import filmfocus.models.requests.AdminRequest;
import filmfocus.models.requests.LoginRequest;
import filmfocus.models.requests.UserRequest;
import filmfocus.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static filmfocus.utils.constants.URIConstants.ADMINS_ID_PATH;
import static filmfocus.utils.constants.URIConstants.ADMINS_PATH;
import static filmfocus.utils.constants.URIConstants.LOGIN_PATH;
import static filmfocus.utils.constants.URIConstants.REGISTRATION_PATH;
import static filmfocus.utils.constants.URIConstants.USERS_ID_PATH;
import static filmfocus.utils.constants.URIConstants.USERS_PATH;

@RestController
public class UserController {

  private static final Logger log = LoggerFactory.getLogger(UserController.class);

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping(LOGIN_PATH)
  public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest request) {
    HttpCookie cookie = userService.login(request);
    log.info("A login request has been submitted");

    return ResponseEntity.status(HttpStatus.OK)
                         .header(HttpHeaders.SET_COOKIE, cookie.toString())
                         .build();
  }

  @PostMapping(REGISTRATION_PATH)
  public ResponseEntity<Void> registerUser(@RequestBody @Valid UserRequest request) throws
    MailjetSocketTimeoutException, MailjetException {
    HttpCookie cookie = userService.registerUser(request);
    log.info("A registration request has been submitted");

    return ResponseEntity.status(HttpStatus.CREATED)
                         .header(HttpHeaders.SET_COOKIE, cookie.toString())
                         .build();
  }

  @PostMapping(ADMINS_PATH)
  public ResponseEntity<Void> registerUserByAdmin(@RequestBody @Valid AdminRequest request) {
    HttpCookie cookie = userService.registerUserByAdmin(request);
    log.info("A registration request by an administrator has been submitted");

    return ResponseEntity.status(HttpStatus.CREATED)
                         .header(HttpHeaders.SET_COOKIE, cookie.toString())
                         .build();
  }

  @GetMapping(value = USERS_PATH, params = "username")
  public ResponseEntity<UserDto> getUserByUsername(@RequestParam String username) {
    UserDto userDto = userService.getUserDtoByUsername(username);
    log.info("User by username was requested from the database");

    return ResponseEntity.ok(userDto);
  }

  @GetMapping(value = USERS_PATH, params = "email")
  public ResponseEntity<UserDto> getUserByEmail(@RequestParam String email) {
    UserDto userDto = userService.getUserDtoByEmail(email);
    log.info("User by email was requested from the database");

    return ResponseEntity.ok(userDto);
  }

  @GetMapping(value = USERS_PATH, params = "roleName")
  public ResponseEntity<List<UserDto>> getUsersByRoleName(@RequestParam String roleName) {
    List<UserDto> userDtos = userService.getUsersDtoByRoleName(roleName);
    log.info("Users by role name were requested from the database");

    return ResponseEntity.ok(userDtos);
  }

  @GetMapping(value = USERS_PATH, params = {"joinDate", "isBefore"})
  public ResponseEntity<List<UserDto>> getUsersByJoinDate(
    @RequestParam("joinDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate joinDate,
    @RequestParam boolean isBefore) {

    List<UserDto> userDtos = userService.getUsersDtosByJoinDate(joinDate, isBefore);
    log.info("All Users by join date were requested from the database");

    return ResponseEntity.ok(userDtos);
  }

  @PutMapping(USERS_ID_PATH)
  public ResponseEntity<UserDto> updateUser(
    @RequestBody @Valid UserRequest request, @PathVariable int id, @RequestParam(required = false) boolean returnOld) {

    UserDto userDto = userService.updateUser(request, id);
    log.info(String.format("User with id %d was updated", id));

    return returnOld ? ResponseEntity.ok(userDto) : ResponseEntity.noContent().build();
  }

  @PutMapping(ADMINS_ID_PATH)
  public ResponseEntity<UserDto> updateUserByAdmin(
    @RequestBody @Valid AdminRequest request, @PathVariable int id, @RequestParam(required = false) boolean returnOld) {

    UserDto userDto = userService.updateUserByAdmin(request, id);
    log.info(String.format("User with id %d was updated by an admin", id));

    return returnOld ? ResponseEntity.ok(userDto) : ResponseEntity.noContent().build();
  }

  @DeleteMapping(USERS_ID_PATH)
  public ResponseEntity<UserDto> deleteUser(@PathVariable int id, @RequestParam(required = false) boolean returnOld) {
    UserDto userDto = userService.deleteUser(id);
    log.info(String.format("User with id %d was deleted", id));

    return returnOld ? ResponseEntity.ok(userDto) : ResponseEntity.noContent().build();
  }

  @PatchMapping("/password-recovery")
  public ResponseEntity<Void> recoverPassword(@RequestParam String username) throws MailjetSocketTimeoutException,
    MailjetException {
    userService.recoverPassword(username);
    log.info(String.format("A password recovery request has been submitted for user %s ", username));
    return ResponseEntity.ok().build();
  }
}