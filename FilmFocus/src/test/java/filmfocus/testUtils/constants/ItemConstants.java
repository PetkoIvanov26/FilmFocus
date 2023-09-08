package filmfocus.testUtils.constants;

import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class ItemConstants {

  public static final int ID = 1;
  public static final String NAME = "Name";
  public static final double PRICE = 13;
  public static final int QUANTITY = 55;
  public static final boolean IS_BELLOW = false;

  private ItemConstants() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }
}
