package filmfocus.services;

import filmfocus.exceptions.NotAuthorizedException;
import filmfocus.exceptions.ReviewNotFoundException;
import filmfocus.mappers.ReviewMapper;
import filmfocus.models.dtos.ReviewDto;
import filmfocus.models.entities.Review;
import filmfocus.repositories.ReviewRepository;
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
import java.util.Optional;

import static filmfocus.testUtils.constants.ReviewConstants.ID;
import static filmfocus.testUtils.constants.ReviewConstants.NOW;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @Mock
    private UserService userService;

    @Mock
    private MovieService movieService;

    @Mock
    private CinemaService cinemaService;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    public void testAddMovieReview_noExceptions_success() {
        Review expected = ReviewFactory.getDefaultReview();

        when(userService.getCurrentUser()).thenReturn(UserFactory.getDefaultUser());
        when(movieService.getMovieById(anyInt())).thenReturn(MovieFactory.getDefaultMovie());
        when(reviewRepository.save(any())).thenReturn(ReviewFactory.getDefaultReview());
        when(movieService.updateMovieAverageRating(anyDouble(), anyInt())).thenReturn(MovieFactory.getDefaultMovieDto());

        Review review = reviewService.addMovieReview(ReviewFactory.getDefaultReviewRequest(), ID);

        assertEquals(expected.getUser(), review.getUser());
        assertEquals(expected.getMovie(), review.getMovie());
        assertEquals(expected.getReviewText(), review.getReviewText());
        assertEquals(NOW, review.getDateModified());
    }

    @Test
    public void testAddMovieReview_reviewsSize1_success() {
        ReviewDto expectedDto = ReviewFactory.getDefaultReviewDto();
        Review expected = ReviewFactory.getDefaultReview();

        when(userService.getCurrentUser()).thenReturn(UserFactory.getDefaultUser());
        when(movieService.getMovieById(anyInt())).thenReturn(MovieFactory.getDefaultMovie());
        when(reviewRepository.save(any())).thenReturn(new Review());
        when(movieService.updateMovieAverageRating(anyDouble(), anyInt())).thenReturn(MovieFactory.getDefaultMovieDto());
        when(reviewRepository.findByMovieId(anyInt())).thenReturn(ReviewFactory.getDefaultReviewList());
        when(reviewMapper.mapReviewToReviewDto(any())).thenReturn(expectedDto);

        Review review = reviewService.addMovieReview(ReviewFactory.getDefaultReviewRequest(), ID);

        assertEquals(expected.getUser(), review.getUser());
        assertEquals(expected.getMovie(), review.getMovie());
        assertEquals(expected.getReviewText(), review.getReviewText());
        assertEquals(NOW, review.getDateModified());
    }

    @Test
    public void testAddCinemaReview_noExceptions_success() {
        Review expected = ReviewFactory.getDefaultReview();

        when(userService.getCurrentUser()).thenReturn(UserFactory.getDefaultUser());
        when(cinemaService.getCinemaById(anyInt())).thenReturn(CinemaFactory.getDefaultCinema());
        when(reviewRepository.save(any())).thenReturn(expected);
        when(cinemaService.updateCinemaAverageRating(anyDouble(), anyInt())).thenReturn(
                CinemaFactory.getDefaultCinemaDto());

        Review review = reviewService.addCinemaReview(ReviewFactory.getDefaultReviewRequest(), ID);

        assertEquals(expected.getUser(), review.getUser());
        assertEquals(expected.getCinema(), review.getCinema());
        assertEquals(expected.getReviewText(), review.getReviewText());
        assertEquals(NOW, review.getDateModified());
    }

    @Test
    public void testGetReviewsByMovieId_noExceptions_success() {
        List<ReviewDto> expectedReviews = ReviewFactory.getDefaultReviewDtoList();

        when(movieService.getMovieById(anyInt())).thenReturn(MovieFactory.getDefaultMovie());
        when(reviewRepository.findByMovieId(anyInt())).thenReturn(ReviewFactory.getDefaultReviewList());
        when(reviewMapper.mapReviewToReviewDto(any())).thenReturn(ReviewFactory.getDefaultReviewDto());

        List<ReviewDto> reviews = reviewService.getReviewsByMovieId(ID);

        assertEquals(expectedReviews, reviews);
    }

    @Test
    public void testGetReviewsByCinemaId_noExceptions_success() {
        List<ReviewDto> expectedReviews = ReviewFactory.getDefaultReviewDtoList();

        when(reviewRepository.findByCinemaId(anyInt())).thenReturn(ReviewFactory.getDefaultReviewList());
        when(reviewMapper.mapReviewToReviewDto(any())).thenReturn(ReviewFactory.getDefaultReviewDto());

        List<ReviewDto> reviews = reviewService.getReviewsByCinemaId(ID);

        assertEquals(expectedReviews, reviews);
    }

    @Test
    public void testGetMovieReviewsByUserId_userAuthorized_success() {
        List<ReviewDto> expectedReviews = ReviewFactory.getDefaultReviewDtoList();

        when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(true);
        when(reviewRepository.findAllByUserIdAndMovieIsNotNullAndCinemaIsNull(anyInt())).thenReturn(
                ReviewFactory.getDefaultReviewList());
        when(reviewMapper.mapReviewListToReviewDtoList(any())).thenReturn(expectedReviews);

        List<ReviewDto> reviews = reviewService.getMovieReviewsByUserId(ID);

        assertEquals(expectedReviews, reviews);
    }

    @Test(expected = NotAuthorizedException.class)
    public void testGetMovieReviewsByUserId_userNotAuthorized_throwsNotAuthorizedException() {
        when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(false);

        reviewService.getMovieReviewsByUserId(ID);
    }

    @Test
    public void testGetCinemaReviewsByUserId_userAuthorized_success() {
        List<ReviewDto> expectedReviews = ReviewFactory.getDefaultReviewDtoList();

        when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(true);
        when(reviewMapper.mapReviewListToReviewDtoList(any())).thenReturn(expectedReviews);

        List<ReviewDto> reviews = reviewService.getCinemaReviewsByUserId(ID);

        assertEquals(expectedReviews, reviews);
    }

    @Test(expected = NotAuthorizedException.class)
    public void testGetCinemaReviewsByUserId_userNotAuthorized_throwsNotAuthorizedException() {
        when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(false);

        reviewService.getCinemaReviewsByUserId(ID);
    }

    @Test
    public void testUpdateReview_noExceptions_success() {
        ReviewDto expected = ReviewFactory.getDefaultReviewDto();

        when(reviewRepository.findById(anyInt())).thenReturn(Optional.of(ReviewFactory.getDefaultReview()));
        when(reviewMapper.mapReviewToReviewDto(any())).thenReturn(expected);
        when(reviewRepository.save(any())).thenReturn(ReviewFactory.getDefaultReview());
        when(movieService.updateMovieAverageRating(anyDouble(), anyInt())).thenReturn(MovieFactory.getDefaultMovieDto());
        CinemaFactory.getDefaultCinemaDto();
        when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(true);

        ReviewDto result = reviewService.updateReview(ReviewFactory.getDefaultReviewRequest(), ID);

        assertEquals(expected, result);
    }

    @Test(expected = NotAuthorizedException.class)
    public void testUpdateReview_userIdDifferent_throwsNotAuthorizedException() {
        when(reviewRepository.findById(anyInt())).thenReturn(Optional.of(ReviewFactory.getDefaultReview()));
        when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(false);

        reviewService.updateReview(ReviewFactory.getDefaultReviewRequest(), ID);
    }

    @Test
    public void testUpdateReview_movieNull_success() {
        Review review = ReviewFactory.getDefaultReview();
        review.setMovie(null);
        ReviewDto expected = ReviewFactory.getDefaultReviewDto();

        when(reviewRepository.findById(anyInt())).thenReturn(Optional.of(review));
        when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(true);
        when(reviewMapper.mapReviewToReviewDto(any())).thenReturn(expected);

        ReviewDto result = reviewService.updateReview(ReviewFactory.getDefaultReviewRequest(), ID);

        assertEquals(expected, result);
    }

    @Test(expected = ReviewNotFoundException.class)
    public void testUpdateReview_ReviewNotFoundException_fail() {
        when(reviewRepository.findById(anyInt())).thenThrow(ReviewNotFoundException.class);

        reviewService.updateReview(ReviewFactory.getDefaultReviewRequest(), ID);
    }

    @Test(expected = ReviewNotFoundException.class)
    public void testUpdateReview_ReviewByUserNotFoundException_fail() {
        when(reviewRepository.findById(anyInt())).thenReturn(Optional.empty());

        reviewService.updateReview(ReviewFactory.getDefaultReviewRequest(), ID);
    }

    @Test
    public void testDeleteReview_noExceptions_success() {
        ReviewDto expected = ReviewFactory.getDefaultReviewDto();

        when(reviewRepository.findById(anyInt())).thenReturn(Optional.of(ReviewFactory.getDefaultReview()));
        when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(true);
        when(reviewMapper.mapReviewToReviewDto(any())).thenReturn(expected);
        when(movieService.updateMovieAverageRating(anyDouble(), anyInt())).thenReturn(MovieFactory.getDefaultMovieDto());
        CinemaFactory.getDefaultCinemaDto();

        ReviewDto result = reviewService.deleteReview(ID);

        assertEquals(expected, result);
    }

    @Test
    public void testDeleteReview_movieNull_success() {
        Review review = ReviewFactory.getDefaultReview();
        review.setMovie(null);
        ReviewDto expected = ReviewFactory.getDefaultReviewDto();

        when(reviewRepository.findById(anyInt())).thenReturn(Optional.of(review));
        when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(true);
        when(reviewMapper.mapReviewToReviewDto(any())).thenReturn(expected);

        ReviewDto result = reviewService.deleteReview(ID);

        assertEquals(expected, result);
    }

    @Test(expected = ReviewNotFoundException.class)
    public void testDeleteReview_ReviewNotFoundException_fail() {
        when(reviewRepository.findById(anyInt())).thenReturn(Optional.empty());

        reviewService.deleteReview(ID);
    }

    @Test(expected = NotAuthorizedException.class)
    public void testDeleteReview_ReviewByUserNotFoundException_fail() {
        when(reviewRepository.findById(anyInt())).thenReturn(Optional.of(ReviewFactory.getDefaultReview()));
        when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(false);

        reviewService.deleteReview(ID);
    }
}
