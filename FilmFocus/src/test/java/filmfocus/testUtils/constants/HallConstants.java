package filmfocus.testUtils.constants;

import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class HallConstants {

  public static final int CAPACITY = 100;
  public static final int ID = 1;

  private HallConstants() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }
}
