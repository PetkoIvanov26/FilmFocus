package filmfocus.testUtils.constants;

import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class CategoryConstants {

  public static final int ID = 1;
  public static final String NAME = "Name";

  private CategoryConstants() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }
}