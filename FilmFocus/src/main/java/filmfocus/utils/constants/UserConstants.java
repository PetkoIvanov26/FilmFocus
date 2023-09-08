package filmfocus.utils.constants;

import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class UserConstants {

  public static final String DEFAULT_GUEST_USERNAME = "anonymousUser";

  private UserConstants() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }
}
