package filmfocus.testUtils.constants;

import filmfocus.models.entities.Cinema;
import filmfocus.testUtils.factories.CinemaFactory;

import java.time.LocalDate;

import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class ProgramConstants {

  public final static int ID = 1;
  public final static LocalDate PAST_DATE = LocalDate.of(2000, 1, 1);
  public final static LocalDate DATE = LocalDate.of(2024, 1, 1);
  public static final Cinema PROGRAM_CINEMA = CinemaFactory.getDefaultCinema();

  private ProgramConstants() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }
}
