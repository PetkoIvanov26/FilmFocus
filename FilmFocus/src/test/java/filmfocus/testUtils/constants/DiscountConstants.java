package filmfocus.testUtils.constants;

import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class DiscountConstants {

  public static final int ID = 1;
  public static final String TYPE = "Military";
  public static final String CODE = "0000";
  public static final int PERCENTAGE = 10;

  private DiscountConstants() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }
}
