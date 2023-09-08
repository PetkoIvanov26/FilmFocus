package filmfocus.testUtils.factories;

import filmfocus.models.dtos.RoleDto;
import filmfocus.models.entities.Role;
import filmfocus.models.requests.RoleRequest;

import java.util.Collections;
import java.util.List;

import static filmfocus.testUtils.constants.RoleConstants.ADMIN;
import static filmfocus.testUtils.constants.RoleConstants.ID;
import static filmfocus.testUtils.constants.RoleConstants.NAME;
import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class RoleFactory {

  private RoleFactory() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }

  public static RoleRequest getDefaultRoleRequest() {
    return new RoleRequest(NAME);
  }

  public static Role getDefaultRole() {
    return new Role(ID, NAME);
  }

  public static Role getDefaultAdminRole() {
    return new Role(ID, ADMIN);
  }

  public static List<Role> getDefaultAdminRoleList() {
    return Collections.singletonList(getDefaultAdminRole());
  }

  public static List<Role> getDefaultRoleList() {
    return Collections.singletonList(getDefaultRole());
  }

  public static RoleDto getDefaultRoleDto() {
    return new RoleDto(ID, NAME);
  }

  public static List<RoleDto> getDefaultRoleDtoList() {
    return Collections.singletonList(getDefaultRoleDto());
  }

  public static List<String> getDefaultRoleNamesList() {
    return Collections.singletonList(NAME);
  }
}