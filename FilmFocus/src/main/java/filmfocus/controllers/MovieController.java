package filmfocus.controllers;

import filmfocus.models.dtos.MovieDto;
import filmfocus.models.entities.Movie;
import filmfocus.models.requests.MovieRequest;
import filmfocus.services.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static filmfocus.utils.constants.URIConstants.CATEGORIES_ID_MOVIES_PATH;
import static filmfocus.utils.constants.URIConstants.MOVIES_ID_PATH;
import static filmfocus.utils.constants.URIConstants.MOVIES_PATH;

@RestController
public class MovieController {

  private static final Logger log = LoggerFactory.getLogger(MovieController.class);

  private final MovieService movieService;

  @Autowired
  public MovieController(MovieService movieService) {
    this.movieService = movieService;
  }

  @PostMapping(MOVIES_PATH)
  public ResponseEntity<Void> addMovie(@RequestBody @Valid MovieRequest movieRequest) {
    Movie movie = movieService.addMovie(movieRequest);
    log.info("A request for a movie to be added has been submitted");

    URI location = UriComponentsBuilder
      .fromUriString(MOVIES_ID_PATH)
      .buildAndExpand(movie.getId())
      .toUri();

    return ResponseEntity.created(location).build();
  }

  @GetMapping(value = MOVIES_PATH, params = "title")
  public ResponseEntity<List<MovieDto>> getMoviesByTitle(
    @RequestParam String title,
    @RequestParam(required = false, defaultValue = "0.0") double minimalRating) {

    List<MovieDto> movieDtoList = movieService.getMoviesByTitle(title, minimalRating);
    log.info("Movies by title was requested from the database");

    return ResponseEntity.ok(movieDtoList);
  }

  @GetMapping(value = MOVIES_PATH, params = "release")
  public ResponseEntity<List<MovieDto>> getMoviesByReleaseDate(
    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate release,
    @RequestParam(required = false) Double rating,
    @RequestParam(required = false) boolean isAfter) {

    List<MovieDto> movieDtoList = movieService.getMoviesByReleaseDate(release, rating, isAfter);
    log.info("Movies by release date after requested from the database");

    return ResponseEntity.ok(movieDtoList);
  }

  @GetMapping(value = MOVIES_PATH, params = "minRating")
  public ResponseEntity<List<MovieDto>> getMoviesByMinRating(@RequestParam double minRating) {
    List<MovieDto> movieDtoList = movieService.getMoviesByMinRating(minRating);
    log.info("Movies by minimum rating requested from the database");

    return ResponseEntity.ok(movieDtoList);
  }

  @GetMapping(value = MOVIES_PATH, params = "imdb")
  public ResponseEntity<String> getMoviesByImdb(@RequestParam String imdb) {
    String movies = movieService.getImdbMovies(imdb);
    log.info("IMDB movies were requested from the database");

    return ResponseEntity.ok(movies);
  }

  @GetMapping(CATEGORIES_ID_MOVIES_PATH)
  public ResponseEntity<List<MovieDto>> getMoviesByCategory(
    @PathVariable("id") Integer categoryId,
    @RequestParam(required = false, defaultValue = "0.0") double minRating) {

    List<MovieDto> movieDtoList = movieService.getMoviesByCategory(categoryId, minRating);
    log.info("Movies by category was requested from the database");

    return ResponseEntity.ok(movieDtoList);
  }

  @PutMapping(MOVIES_ID_PATH)
  public ResponseEntity<MovieDto> updateMovie(
    @RequestBody @Valid MovieRequest movieRequest,
    @PathVariable int id,
    @RequestParam(required = false) boolean returnOld) {

    MovieDto movieDto = movieService.updateMovie(movieRequest, id);
    log.info(String.format("Movie with id %d was updated", id));

    return returnOld ? ResponseEntity.ok(movieDto) : ResponseEntity.noContent().build();
  }

  @DeleteMapping(MOVIES_ID_PATH)
  public ResponseEntity<MovieDto> deleteMovie(
    @PathVariable int id,
    @RequestParam(required = false) boolean returnOld) {

    MovieDto movieDto = movieService.deleteMovie(id);
    log.info(String.format("Movie with id %d was deleted", id));

    return returnOld ? ResponseEntity.ok(movieDto) : ResponseEntity.noContent().build();
  }
}
