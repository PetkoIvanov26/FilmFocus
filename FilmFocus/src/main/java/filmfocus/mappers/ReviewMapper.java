package filmfocus.mappers;

import filmfocus.models.dtos.ReviewDto;
import filmfocus.models.entities.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ReviewMapper {

  private static final Logger log = LoggerFactory.getLogger(ReviewMapper.class);
  private final MovieMapper movieMapper;
  private final CinemaMapper cinemaMapper;
  private final UserMapper userMapper;

  @Autowired
  public ReviewMapper(MovieMapper movieMapper, CinemaMapper cinemaMapper, UserMapper userMapper) {
    this.movieMapper = movieMapper;
    this.cinemaMapper = cinemaMapper;
    this.userMapper = userMapper;
  }

  public ReviewDto mapReviewToReviewDto(Review review) {
    log.info(String.format("The review %d is being mapped to a review DTO", review.getId()));

    if (Objects.nonNull(review.getMovie())) {
      return new ReviewDto(review.getId(), review.getRating(), review.getReviewText(), review.getDateModified(),
                           movieMapper.mapMovieToMovieDto(review.getMovie()), null,
                           userMapper.mapUserToUserDto(review.getUser()));
    }

    return new ReviewDto(review.getId(), review.getRating(), review.getReviewText(), review.getDateModified(), null,
                         cinemaMapper.mapCinemaToCinemaDto(review.getCinema()),
                         userMapper.mapUserToUserDto(review.getUser()));
  }

  public List<ReviewDto> mapReviewListToReviewDtoList(List<Review> reviews) {
    return reviews.stream()
                  .map(this::mapReviewToReviewDto)
                  .collect(Collectors.toList());
  }
}
