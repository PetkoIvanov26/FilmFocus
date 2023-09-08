package filmfocus.testUtils.constants;

import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class RoleConstants {

  public static final int ID = 1;
  public static final String NAME = "USER";
  public static final String ADMIN = "ADMIN";

  private RoleConstants() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }
}
