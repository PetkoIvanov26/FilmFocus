package filmfocus.testUtils.constants;

import java.time.Duration;
import java.time.LocalDate;

import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class MovieConstants {

  public static final String TITLE = "Movie title";
  public static final String DESCRIPTION = "Movie description";
  public static final LocalDate RELEASE_DATE = LocalDate.of(2024, 1, 1);
  public static final Duration RUNTIME = Duration.ofHours(24);
  public static final int ID = 1;
  public static final double RATING = 5;

  private MovieConstants() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }
}
