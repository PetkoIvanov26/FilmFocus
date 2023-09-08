package filmfocus.testUtils.constants;

import filmfocus.models.dtos.CinemaDto;
import filmfocus.models.dtos.MovieDto;
import filmfocus.models.dtos.UserDto;
import filmfocus.models.entities.Cinema;
import filmfocus.models.entities.Movie;
import filmfocus.models.entities.User;
import filmfocus.testUtils.factories.CinemaFactory;
import filmfocus.testUtils.factories.MovieFactory;
import filmfocus.testUtils.factories.UserFactory;

import java.time.LocalDate;

import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class ReviewConstants {

  public static final int ID = 1;
  public static final double RATING = 1.1;
  public static final String REVIEW_TEXT = "review text";
  public static final LocalDate DATE_MODIFIED = LocalDate.of(2024, 1, 1);
  public static final LocalDate NOW = LocalDate.now();
  public static final Movie REVIEW_MOVIE = MovieFactory.getDefaultMovie();
  public static final MovieDto REVIEW_MOVIE_DTO = MovieFactory.getDefaultMovieDto();
  public static final Cinema REVIEW_CINEMA = CinemaFactory.getDefaultCinema();
  public static final CinemaDto REVIEW_CINEMA_DTO = CinemaFactory.getDefaultCinemaDto();
  public static final User REVIEW_USER = UserFactory.getDefaultUser();
  public static final UserDto REVIEW_USER_DTO = UserFactory.getDefaultUserDto();

  private ReviewConstants() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }
}
