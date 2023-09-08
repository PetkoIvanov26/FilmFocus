package filmfocus.mappers;

import filmfocus.models.dtos.MovieDto;
import filmfocus.models.entities.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieMapper {

  private static final Logger log = LoggerFactory.getLogger(MovieMapper.class);

  private final CategoryMapper categoryMapper;

  @Autowired
  public MovieMapper(CategoryMapper categoryMapper) {
    this.categoryMapper = categoryMapper;
  }

  public MovieDto mapMovieToMovieDto(Movie movie) {
    log.info(String.format("The movie %s is being mapped to a movie DTO", movie.getTitle()));
    return new MovieDto(movie.getId(), movie.getTitle(), movie.getAverageRating(), movie.getDescription(),
                        movie.getReleaseDate(), movie.getRuntime(),
                        categoryMapper.mapCategoryToCategoryDto(movie.getCategory()));
  }

  public List<MovieDto> mapMovieListToMovieDtoList(List<Movie> movies) {
    return movies.stream()
                 .map(this::mapMovieToMovieDto)
                 .collect(Collectors.toList());
  }
}