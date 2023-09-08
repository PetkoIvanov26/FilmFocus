package filmfocus.testUtils.constants;

import filmfocus.models.dtos.HallDto;
import filmfocus.models.dtos.MovieDto;
import filmfocus.models.dtos.ProgramDto;
import filmfocus.models.entities.Hall;
import filmfocus.models.entities.Movie;
import filmfocus.models.entities.Program;
import filmfocus.testUtils.factories.HallFactory;
import filmfocus.testUtils.factories.MovieFactory;
import filmfocus.testUtils.factories.ProgramFactory;

import java.time.LocalTime;

import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class ProjectionConstants {

  public static final int ID = 1;
  public static final double PRICE = 10;
  public static final LocalTime START_TIME = LocalTime.of(1, 0, 1);
  public static final HallDto PROJECTION_HALL_DTO = HallFactory.getDefaultHallDto();
  public static final MovieDto PROJECTION_MOVIE_DTO = MovieFactory.getDefaultMovieDto();
  public static final ProgramDto PROJECTION_PROGRAM_DTO = ProgramFactory.getDefaultProgramDto();

  private ProjectionConstants() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }
}
