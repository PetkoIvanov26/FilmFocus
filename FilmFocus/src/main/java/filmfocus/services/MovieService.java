package filmfocus.services;

import filmfocus.exceptions.DateNotValidException;
import filmfocus.exceptions.MovieAlreadyExistsException;
import filmfocus.exceptions.MovieNotFoundException;
import filmfocus.mappers.MovieMapper;
import filmfocus.models.dtos.MovieDto;
import filmfocus.models.entities.Movie;
import filmfocus.models.requests.MovieRequest;
import filmfocus.repositories.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static filmfocus.utils.constants.ExceptionMessages.DATE_NOT_VALID_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.MOVIE_ALREADY_EXISTS_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.MOVIE_NOT_FOUND_MESSAGE;

@Service
public class MovieService {

  private static final Logger log = LoggerFactory.getLogger(MovieService.class);

  private final MovieRepository movieRepository;
  private final MovieMapper movieMapper;
  private final CategoryService categoryService;
  private final ImdbService imdbService;

  @Autowired
  public MovieService(
    MovieRepository movieRepository, MovieMapper movieMapper, CategoryService categoryService,
    ImdbService imdbService) {
    this.movieRepository = movieRepository;
    this.movieMapper = movieMapper;
    this.categoryService = categoryService;
    this.imdbService = imdbService;
  }

  public Movie addMovie(MovieRequest request) {
    log.info("An attempt to add a new movie in the database");

    if (isDateNotValid(request.getReleaseDate())) {

      log.error(String.format("Exception caught: %s", DATE_NOT_VALID_MESSAGE));

      throw new DateNotValidException(DATE_NOT_VALID_MESSAGE);
    }

    this.movieRepository.findByTitle(request.getTitle()).ifPresent(movie -> {
      log.error(String.format("Exception caught: %s", MOVIE_ALREADY_EXISTS_MESSAGE));

      throw new MovieAlreadyExistsException(MOVIE_ALREADY_EXISTS_MESSAGE);
    });

    return movieRepository.save(
      new Movie(request.getTitle(), request.getDescription(), request.getReleaseDate(),
                request.getRuntime(), categoryService.getCategoryById(request.getCategoryId()))
    );
  }

  public List<MovieDto> getMoviesByTitle(String title, double minRating) {
    if (title == null || title.isEmpty()) {

      log.error(
        "Exception caught: Required request parameter 'title' for method parameter type String is present but " +
        "converted to null");

      throw new IllegalArgumentException(
        "Required request parameter 'title' for method parameter type String is present but converted to null");
    }

    if (minRating == 0.0) {
      log.info(String.format("All movies with title %s were requested from the database", title));

      return movieMapper.mapMovieListToMovieDtoList(movieRepository.findByTitleContaining(title));
    } else {
      log.info(String.format("All movies with title %s and rating %.2f were requested from the database",
                             title, minRating));

      return movieMapper.mapMovieListToMovieDtoList(
        movieRepository.findByTitleContainingAndAverageRatingGreaterThanEqual(title, minRating)
      );
    }
  }

  public List<MovieDto> getMoviesByCategory(Integer categoryId, double minRating) {
    if (minRating == 0.0) {
      log.info(String.format("All movies with category id %d were requested from the database", categoryId));

      return movieMapper.mapMovieListToMovieDtoList(movieRepository.findByCategoryId(categoryId));
    } else {
      log.info(String.format("All movies with category id %d and rating %.2f were requested from the database",
                             categoryId, minRating));

      return movieMapper.mapMovieListToMovieDtoList(
        movieRepository.findByCategoryIdAndAverageRatingGreaterThanEqual(categoryId, minRating)
      );
    }
  }

  public List<MovieDto> getMoviesByReleaseDate(LocalDate releaseDate, Double minRating, boolean isAfter) {
    List<Movie> movies;

    log.info(String.format("All movies with releaseDate %s were requested from the database", releaseDate));

    if (isAfter) {
      movies = minRating != null ?
               movieRepository.findByReleaseDateAfterAndAverageRatingGreaterThanEqual(releaseDate, minRating) :
               movieRepository.findByReleaseDateAfter(releaseDate);
    } else {
      movies = minRating != null ?
               movieRepository.findByReleaseDateBeforeAndAverageRatingGreaterThanEqual(releaseDate, minRating) :
               movieRepository.findByReleaseDateBefore(releaseDate);
    }

    return movieMapper.mapMovieListToMovieDtoList(movies);
  }

  public List<MovieDto> getMoviesByMinRating(double rating) {
    log.info(String.format("All movies with rating of %.2f were requested from the database", rating));

    return movieMapper.mapMovieListToMovieDtoList(movieRepository.findByAverageRatingGreaterThanEqual(rating));
  }

  public Movie getMovieByTitle(String title) {
    return movieRepository.findByTitle(title).orElseThrow(() -> {

      log.error(String.format("Exception caught: %s", MOVIE_NOT_FOUND_MESSAGE));

      throw new MovieNotFoundException(MOVIE_NOT_FOUND_MESSAGE);
    });
  }

  public MovieDto getMovieDtoById(int id) {
    log.info(String.format("An attempt to extract a movie DTO with an id %d from the database", id));

    return movieMapper.mapMovieToMovieDto(getMovieById(id));
  }

  public Movie getMovieById(int id) {
    log.info(String.format("An attempt to extract a movie with an id %d from the database", id));

    return movieRepository.findById(id).orElseThrow(() -> {

      log.error(String.format("Exception caught: %s", MOVIE_NOT_FOUND_MESSAGE));

      throw new MovieNotFoundException(MOVIE_NOT_FOUND_MESSAGE);
    });
  }

  public List<Integer> getIdsOfMoviesByTitle(String title) {
    log.info(String.format("All movies IDs with title %s were requested from the database", title));

    List<Movie> movies = movieRepository.findByTitleContaining(title);

    return movies.stream()
                 .map(Movie::getId)
                 .collect(Collectors.toList());
  }

  public String getImdbMovies(String filter) {
    return imdbService.getMovies(filter);
  }

  public MovieDto updateMovie(MovieRequest request, int id) {
    MovieDto movieDto = getMovieDtoById(id);

    if (isDateNotValid(request.getReleaseDate())) {
      log.error(String.format("Exception caught: %s", DATE_NOT_VALID_MESSAGE));

      throw new DateNotValidException(DATE_NOT_VALID_MESSAGE);
    }

    movieRepository.save(new Movie(id, request.getTitle(), request.getDescription(), request.getReleaseDate(),
                                   request.getRuntime(), categoryService.getCategoryById(request.getCategoryId())));

    log.info(String.format("Movie with an id %d has been updated", id));

    return movieDto;
  }

  public MovieDto updateMovieAverageRating(double newRating, int movieId) {
    Movie movie = getMovieById(movieId);

    MovieDto movieDto = movieMapper.mapMovieToMovieDto(movie);

    movie.setAverageRating(newRating);
    movieDto.setAverageRating(newRating);

    movieRepository.save(movie);

    log.info(String.format("Updated average rating for movie with id %d", movieId));

    return movieDto;
  }

  public MovieDto deleteMovie(int id) {
    MovieDto movieDto = getMovieDtoById(id);

    movieRepository.deleteById(id);

    log.info(String.format("Movie with an id %d has been deleted", id));

    return movieDto;
  }

  public boolean isDateNotValid(LocalDate releaseDate) {
    return releaseDate.isBefore(LocalDate.now());
  }
}
