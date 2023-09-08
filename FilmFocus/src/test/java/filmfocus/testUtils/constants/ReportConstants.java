package filmfocus.testUtils.constants;

import java.time.LocalDate;

import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class ReportConstants {

  public static final int ID = 1;
  public static final double INCOMES = 10000;
  public static final LocalDate START_DATE = LocalDate.of(1900, 1, 1);
  public static final LocalDate END_DATE = LocalDate.of(2100, 1, 1);

  private ReportConstants() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }
}
