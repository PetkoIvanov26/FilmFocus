package filmfocus.testUtils.factories;

import filmfocus.models.dtos.UserDto;
import filmfocus.models.entities.User;
import filmfocus.models.requests.AdminRequest;
import filmfocus.models.requests.UserRequest;

import java.util.Collections;
import java.util.List;

import static filmfocus.testUtils.constants.UserConstants.EMAIL;
import static filmfocus.testUtils.constants.UserConstants.FIRST_NAME;
import static filmfocus.testUtils.constants.UserConstants.ID;
import static filmfocus.testUtils.constants.UserConstants.JOIN_DATE;
import static filmfocus.testUtils.constants.UserConstants.LAST_NAME;
import static filmfocus.testUtils.constants.UserConstants.PASSWORD;
import static filmfocus.testUtils.constants.UserConstants.USERNAME;
import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class UserFactory {

  private UserFactory() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }

  public static AdminRequest getDefaultAdminRequest() {
    return new AdminRequest(USERNAME, PASSWORD, EMAIL, FIRST_NAME, LAST_NAME, RoleFactory.getDefaultRoleNamesList());
  }

  public static UserRequest getDefaultUserRequest() {
    return new UserRequest(USERNAME, PASSWORD, EMAIL, FIRST_NAME, LAST_NAME);
  }

  public static User getDefaultUser() {
    return new User(ID, USERNAME, PASSWORD, EMAIL, FIRST_NAME, LAST_NAME, JOIN_DATE, RoleFactory.getDefaultRoleList());
  }

  public static User getDefaultAdmin() {
    return new User(ID, USERNAME, PASSWORD, EMAIL, FIRST_NAME, LAST_NAME, JOIN_DATE,
                    RoleFactory.getDefaultAdminRoleList());
  }

  public static List<User> getDefaultUserList() {
    return Collections.singletonList(getDefaultUser());
  }

  public static UserDto getDefaultUserDto() {
    return new UserDto(ID, USERNAME, EMAIL, FIRST_NAME, LAST_NAME, JOIN_DATE, RoleFactory.getDefaultRoleDtoList());
  }

  public static List<UserDto> getDefaultUserDtoList() {
    return Collections.singletonList(getDefaultUserDto());
  }
}
