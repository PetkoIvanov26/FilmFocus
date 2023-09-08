package filmfocus.testUtils.factories;

import filmfocus.models.dtos.MovieDto;
import filmfocus.models.entities.Movie;
import filmfocus.models.requests.MovieRequest;

import java.util.Collections;
import java.util.List;

import static filmfocus.testUtils.constants.MovieConstants.DESCRIPTION;
import static filmfocus.testUtils.constants.MovieConstants.ID;
import static filmfocus.testUtils.constants.MovieConstants.RATING;
import static filmfocus.testUtils.constants.MovieConstants.RELEASE_DATE;
import static filmfocus.testUtils.constants.MovieConstants.RUNTIME;
import static filmfocus.testUtils.constants.MovieConstants.TITLE;
import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class MovieFactory {

  private MovieFactory() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }

  public static MovieRequest getDefaultMovieRequest() {
    return new MovieRequest(TITLE, DESCRIPTION, RELEASE_DATE, RUNTIME, ID);
  }

  public static Movie getDefaultMovie() {
    return new Movie(ID, TITLE, RATING, DESCRIPTION, RELEASE_DATE, RUNTIME,
                     CategoryFactory.getDefaultCategory());
  }

  public static List<Movie> getDefaultMovieList() {
    return Collections.singletonList(getDefaultMovie());
  }

  public static MovieDto getDefaultMovieDto() {
    return new MovieDto(ID, TITLE, RATING, DESCRIPTION, RELEASE_DATE, RUNTIME,
                        CategoryFactory.getDefaultCategoryDto());
  }

  public static List<MovieDto> getDefaultMovieDtoList() {
    return Collections.singletonList(getDefaultMovieDto());
  }
}
