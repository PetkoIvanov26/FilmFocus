package filmfocus.mappers;

import filmfocus.models.dtos.CinemaDto;
import filmfocus.models.dtos.MovieDto;
import filmfocus.models.dtos.ReviewDto;
import filmfocus.testUtils.factories.CinemaFactory;
import filmfocus.testUtils.factories.MovieFactory;
import filmfocus.testUtils.factories.ReviewFactory;
import filmfocus.testUtils.factories.UserFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static filmfocus.testUtils.constants.ReviewConstants.DATE_MODIFIED;
import static filmfocus.testUtils.constants.ReviewConstants.ID;
import static filmfocus.testUtils.constants.ReviewConstants.RATING;
import static filmfocus.testUtils.constants.ReviewConstants.REVIEW_TEXT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReviewMapperTest {

    @Mock
    private MovieMapper movieMapper;
    @Mock
    private CinemaMapper cinemaMapper;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private ReviewMapper reviewMapper;

    @Test
    public void testMapReviewToReviewDto_cinemaNull_success() {
        MovieDto movie = MovieFactory.getDefaultMovieDto();

        when(movieMapper.mapMovieToMovieDto(any())).thenReturn(movie);
        when(userMapper.mapUserToUserDto(any())).thenReturn(UserFactory.getDefaultUserDto());

        ReviewDto reviewDto = reviewMapper.mapReviewToReviewDto(ReviewFactory.getDefaultReview());

        assertEquals(ID, reviewDto.getId());
        assertEquals(RATING, reviewDto.getRating(), 0.0);
        assertEquals(REVIEW_TEXT, reviewDto.getReviewText());
        assertEquals(DATE_MODIFIED, reviewDto.getDateModified());
        assertEquals(movie, reviewDto.getMovie());
        assertNull(reviewDto.getCinema());
        assertEquals(UserFactory.getDefaultUserDto(), reviewDto.getUser());
    }

    @Test
    public void testMapReviewToReviewDto_movieNull_success() {
        CinemaDto cinema = CinemaFactory.getDefaultCinemaDto();

        when(cinemaMapper.mapCinemaToCinemaDto(any())).thenReturn(cinema);
        when(userMapper.mapUserToUserDto(any())).thenReturn(UserFactory.getDefaultUserDto());

        ReviewDto reviewDto = reviewMapper.mapReviewToReviewDto(ReviewFactory.getDefaultReviewWithCinema());

        assertEquals(ID, reviewDto.getId());
        assertEquals(RATING, reviewDto.getRating(), 0.0);
        assertEquals(REVIEW_TEXT, reviewDto.getReviewText());
        assertEquals(DATE_MODIFIED, reviewDto.getDateModified());
        assertNull(reviewDto.getMovie());
        assertEquals(cinema, reviewDto.getCinema());
        assertEquals(UserFactory.getDefaultUserDto(), reviewDto.getUser());
    }

    @Test
    public void testMapReviewListToReviewDtoList_cinemaNull_success() {
        MovieDto movie = MovieFactory.getDefaultMovieDto();
        when(movieMapper.mapMovieToMovieDto(any())).thenReturn(movie);
        when(userMapper.mapUserToUserDto(any())).thenReturn(UserFactory.getDefaultUserDto());

        List<ReviewDto> reviewDtos = reviewMapper.mapReviewListToReviewDtoList(ReviewFactory.getDefaultReviewList());
        ReviewDto reviewDto = reviewDtos.get(0);

        assertEquals(ID, reviewDto.getId());
        assertEquals(RATING, reviewDto.getRating(), 0.0);
        assertEquals(REVIEW_TEXT, reviewDto.getReviewText());
        assertEquals(DATE_MODIFIED, reviewDto.getDateModified());
        assertEquals(movie, reviewDto.getMovie());
        assertNull(reviewDto.getCinema());
        assertEquals(UserFactory.getDefaultUserDto(), reviewDto.getUser());
    }

    @Test
    public void testMapReviewListToReviewDtoList_movieNull_success() {
        CinemaDto cinema = CinemaFactory.getDefaultCinemaDto();

        when(cinemaMapper.mapCinemaToCinemaDto(any())).thenReturn(cinema);
        when(userMapper.mapUserToUserDto(any())).thenReturn(UserFactory.getDefaultUserDto());

        List<ReviewDto> reviewDtos = reviewMapper.mapReviewListToReviewDtoList(ReviewFactory.getDefaultReviewListWithCinema());
        ReviewDto reviewDto = reviewDtos.get(0);

        assertEquals(ID, reviewDto.getId());
        assertEquals(RATING, reviewDto.getRating(), 0.0);
        assertEquals(REVIEW_TEXT, reviewDto.getReviewText());
        assertEquals(DATE_MODIFIED, reviewDto.getDateModified());
        assertNull(reviewDto.getMovie());
        assertEquals(cinema, reviewDto.getCinema());
        assertEquals(UserFactory.getDefaultUserDto(), reviewDto.getUser());
    }
}
