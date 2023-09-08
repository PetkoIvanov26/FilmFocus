package filmfocus.utils.constants;

import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class RoleConstants {

  public static final String DEFAULT_ADMIN_ROLE = "ADMIN";
  public static final String DEFAULT_VENDOR_ROLE = "VENDOR";
  public static final String DEFAULT_USER_ROLE = "USER";

  private RoleConstants() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }
}
