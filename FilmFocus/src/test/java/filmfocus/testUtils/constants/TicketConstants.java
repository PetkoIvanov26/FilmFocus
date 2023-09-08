package filmfocus.testUtils.constants;

import java.time.LocalDate;

import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class TicketConstants {

  public static final int ID = 1;
  public static final LocalDate DATE_OF_PURCHASE = LocalDate.of(1999, 1, 1);

  private TicketConstants() throws IllegalAccessError {
    throw new IllegalAccessError(NON_INSTANTIABLE_CLASS_MESSAGE);
  }
}
