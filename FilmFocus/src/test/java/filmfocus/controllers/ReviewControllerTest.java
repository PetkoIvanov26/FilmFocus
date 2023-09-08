package filmfocus.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import filmfocus.services.ReviewService;
import filmfocus.testUtils.constants.CinemaConstants;
import filmfocus.testUtils.constants.MovieConstants;
import filmfocus.testUtils.constants.UserConstants;
import filmfocus.testUtils.factories.CinemaFactory;
import filmfocus.testUtils.factories.ReviewFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static filmfocus.testUtils.constants.ReviewConstants.DATE_MODIFIED;
import static filmfocus.testUtils.constants.ReviewConstants.ID;
import static filmfocus.testUtils.constants.ReviewConstants.RATING;
import static filmfocus.testUtils.constants.ReviewConstants.REVIEW_TEXT;
import static filmfocus.testUtils.constants.UserConstants.DAY;
import static filmfocus.testUtils.constants.UserConstants.MONTH;
import static filmfocus.testUtils.constants.UserConstants.YEAR;
import static filmfocus.utils.constants.URIConstants.CINEMAS_ID_REVIEWS_PATH;
import static filmfocus.utils.constants.URIConstants.MOVIES_ID_REVIEWS_PATH;
import static filmfocus.utils.constants.URIConstants.REVIEWS_ID_PATH;
import static filmfocus.utils.constants.URIConstants.USERS_ID_CINEMAS_REVIEWS_PATH;
import static filmfocus.utils.constants.URIConstants.USERS_ID_MOVIES_REVIEWS_PATH;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@AutoConfigureMockMvc
public class ReviewControllerTest {

  private static final String REVIEW_ID_PATH_PLACEHOLDER = "/reviews/1";
  private static final String RETURN_OLD = "returnOld";

  private final static ObjectMapper objectMapper = new ObjectMapper();
  private MockMvc mockMvc;

  @Mock
  private ReviewService reviewService;

  @InjectMocks
  private ReviewController reviewController;

  @Before
  public void setup() {
    mockMvc = MockMvcBuilders
      .standaloneSetup(reviewController)
      .build();
  }

  @Test
  public void testAddCinemaReview_reviewAdded_success() throws Exception {
    when(reviewService.addCinemaReview(any(), anyInt())).thenReturn(ReviewFactory.getDefaultReview());
    String json = objectMapper.writeValueAsString(ReviewFactory.getDefaultReviewRequest());

    mockMvc.perform(post(CINEMAS_ID_REVIEWS_PATH, CinemaConstants.ID)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isCreated())
           .andExpect(header().string("Location", REVIEW_ID_PATH_PLACEHOLDER));
  }

  @Test
  public void testAddMovieReview_reviewAdded_success() throws Exception {
    when(reviewService.addMovieReview(any(), anyInt())).thenReturn(ReviewFactory.getDefaultReview());
    String json = objectMapper.writeValueAsString(ReviewFactory.getDefaultReviewRequest());

    mockMvc.perform(post(MOVIES_ID_REVIEWS_PATH, MovieConstants.ID)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isCreated())
           .andExpect(header().string("Location", REVIEW_ID_PATH_PLACEHOLDER));
  }

  @Test
  public void testGetReviewsByCinemaId_noExceptions_success() throws Exception {
    when(reviewService.getReviewsByCinemaId(anyInt())).thenReturn(ReviewFactory.getDefaultReviewDtoList());

    mockMvc.perform(get(CINEMAS_ID_REVIEWS_PATH, CinemaConstants.ID))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(ID))
           .andExpect(jsonPath("$[0].rating").value(RATING))
           .andExpect(jsonPath("$[0].reviewText").value(REVIEW_TEXT))
           .andExpect(jsonPath("$[0].dateModified[0]", is(DATE_MODIFIED.getYear())))
           .andExpect(jsonPath("$[0].dateModified[1]", is(DATE_MODIFIED.getMonthValue())))
           .andExpect(jsonPath("$[0].dateModified[2]", is(DATE_MODIFIED.getDayOfMonth())))
           .andExpect(jsonPath("$[0].movie.id").value(MovieConstants.ID))
           .andExpect(jsonPath("$[0].movie.title").value(MovieConstants.TITLE))
           .andExpect(jsonPath("$[0].movie.description").value(MovieConstants.DESCRIPTION))
           .andExpect(jsonPath("$[0].movie.releaseDate[0]", is(MovieConstants.RELEASE_DATE.getYear())))
           .andExpect(jsonPath("$[0].movie.releaseDate[1]", is(MovieConstants.RELEASE_DATE.getMonthValue())))
           .andExpect(jsonPath("$[0].movie.releaseDate[2]", is(MovieConstants.RELEASE_DATE.getDayOfMonth())))
           .andExpect(jsonPath("$[0].cinema").value(CinemaFactory.getDefaultCinemaDto()))
           .andExpect(jsonPath("$[0].user.id").value(UserConstants.ID))
           .andExpect(jsonPath("$[0].user.username").value(UserConstants.USERNAME))
           .andExpect(jsonPath("$[0].user.email").value(UserConstants.EMAIL))
           .andExpect(jsonPath("$[0].user.firstName").value(UserConstants.FIRST_NAME))
           .andExpect(jsonPath("$[0].user.lastName").value(UserConstants.LAST_NAME))
           .andExpect(jsonPath("$[0].user.joinDate[0]").value(YEAR))
           .andExpect(jsonPath("$[0].user.joinDate[1]").value(MONTH))
           .andExpect(jsonPath("$[0].user.joinDate[2]").value(DAY));
  }

  @Test
  public void testGetReviewsByMovieId_noExceptions_success() throws Exception {
    when(reviewService.getReviewsByMovieId(anyInt())).thenReturn(ReviewFactory.getDefaultReviewDtoList());

    mockMvc.perform(get(MOVIES_ID_REVIEWS_PATH, MovieConstants.ID))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(ID))
           .andExpect(jsonPath("$[0].rating").value(RATING))
           .andExpect(jsonPath("$[0].reviewText").value(REVIEW_TEXT))
           .andExpect(jsonPath("$[0].dateModified[0]", is(DATE_MODIFIED.getYear())))
           .andExpect(jsonPath("$[0].dateModified[1]", is(DATE_MODIFIED.getMonthValue())))
           .andExpect(jsonPath("$[0].dateModified[2]", is(DATE_MODIFIED.getDayOfMonth())))
           .andExpect(jsonPath("$[0].movie.id").value(MovieConstants.ID))
           .andExpect(jsonPath("$[0].movie.title").value(MovieConstants.TITLE))
           .andExpect(jsonPath("$[0].movie.description").value(MovieConstants.DESCRIPTION))
           .andExpect(jsonPath("$[0].movie.releaseDate[0]", is(MovieConstants.RELEASE_DATE.getYear())))
           .andExpect(jsonPath("$[0].movie.releaseDate[1]", is(MovieConstants.RELEASE_DATE.getMonthValue())))
           .andExpect(jsonPath("$[0].movie.releaseDate[2]", is(MovieConstants.RELEASE_DATE.getDayOfMonth())))
           .andExpect(jsonPath("$[0].cinema").value(CinemaFactory.getDefaultCinemaDto()))
           .andExpect(jsonPath("$[0].user.id").value(UserConstants.ID))
           .andExpect(jsonPath("$[0].user.username").value(UserConstants.USERNAME))
           .andExpect(jsonPath("$[0].user.email").value(UserConstants.EMAIL))
           .andExpect(jsonPath("$[0].user.firstName").value(UserConstants.FIRST_NAME))
           .andExpect(jsonPath("$[0].user.lastName").value(UserConstants.LAST_NAME))
           .andExpect(jsonPath("$[0].user.joinDate[0]").value(YEAR))
           .andExpect(jsonPath("$[0].user.joinDate[1]").value(MONTH))
           .andExpect(jsonPath("$[0].user.joinDate[2]").value(DAY));
  }

  @Test
  public void testGetMovieReviewsByUserId_noExceptions_success() throws Exception {
    when(reviewService.getMovieReviewsByUserId(anyInt())).thenReturn(ReviewFactory.getDefaultReviewDtoList());

    mockMvc.perform(get(USERS_ID_MOVIES_REVIEWS_PATH, UserConstants.ID))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(ID))
           .andExpect(jsonPath("$[0].rating").value(RATING))
           .andExpect(jsonPath("$[0].reviewText").value(REVIEW_TEXT))
           .andExpect(jsonPath("$[0].dateModified[0]", is(DATE_MODIFIED.getYear())))
           .andExpect(jsonPath("$[0].dateModified[1]", is(DATE_MODIFIED.getMonthValue())))
           .andExpect(jsonPath("$[0].dateModified[2]", is(DATE_MODIFIED.getDayOfMonth())))
           .andExpect(jsonPath("$[0].movie.id").value(MovieConstants.ID))
           .andExpect(jsonPath("$[0].movie.title").value(MovieConstants.TITLE))
           .andExpect(jsonPath("$[0].movie.description").value(MovieConstants.DESCRIPTION))
           .andExpect(jsonPath("$[0].movie.releaseDate[0]", is(MovieConstants.RELEASE_DATE.getYear())))
           .andExpect(jsonPath("$[0].movie.releaseDate[1]", is(MovieConstants.RELEASE_DATE.getMonthValue())))
           .andExpect(jsonPath("$[0].movie.releaseDate[2]", is(MovieConstants.RELEASE_DATE.getDayOfMonth())))
           .andExpect(jsonPath("$[0].cinema").value(CinemaFactory.getDefaultCinemaDto()))
           .andExpect(jsonPath("$[0].user.id").value(UserConstants.ID))
           .andExpect(jsonPath("$[0].user.username").value(UserConstants.USERNAME))
           .andExpect(jsonPath("$[0].user.email").value(UserConstants.EMAIL))
           .andExpect(jsonPath("$[0].user.firstName").value(UserConstants.FIRST_NAME))
           .andExpect(jsonPath("$[0].user.lastName").value(UserConstants.LAST_NAME))
           .andExpect(jsonPath("$[0].user.joinDate[0]").value(YEAR))
           .andExpect(jsonPath("$[0].user.joinDate[1]").value(MONTH))
           .andExpect(jsonPath("$[0].user.joinDate[2]").value(DAY));
  }

  @Test
  public void testGetCinemaReviewsByUserId_noExceptions_success() throws Exception {
    when(reviewService.getCinemaReviewsByUserId(anyInt())).thenReturn(ReviewFactory.getDefaultReviewDtoList());

    mockMvc.perform(get(USERS_ID_CINEMAS_REVIEWS_PATH, UserConstants.ID))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(ID))
           .andExpect(jsonPath("$[0].rating").value(RATING))
           .andExpect(jsonPath("$[0].reviewText").value(REVIEW_TEXT))
           .andExpect(jsonPath("$[0].dateModified[0]", is(DATE_MODIFIED.getYear())))
           .andExpect(jsonPath("$[0].dateModified[1]", is(DATE_MODIFIED.getMonthValue())))
           .andExpect(jsonPath("$[0].dateModified[2]", is(DATE_MODIFIED.getDayOfMonth())))
           .andExpect(jsonPath("$[0].movie.id").value(MovieConstants.ID))
           .andExpect(jsonPath("$[0].movie.title").value(MovieConstants.TITLE))
           .andExpect(jsonPath("$[0].movie.description").value(MovieConstants.DESCRIPTION))
           .andExpect(jsonPath("$[0].movie.releaseDate[0]", is(MovieConstants.RELEASE_DATE.getYear())))
           .andExpect(jsonPath("$[0].movie.releaseDate[1]", is(MovieConstants.RELEASE_DATE.getMonthValue())))
           .andExpect(jsonPath("$[0].movie.releaseDate[2]", is(MovieConstants.RELEASE_DATE.getDayOfMonth())))
           .andExpect(jsonPath("$[0].cinema").value(CinemaFactory.getDefaultCinemaDto()))
           .andExpect(jsonPath("$[0].user.id").value(UserConstants.ID))
           .andExpect(jsonPath("$[0].user.username").value(UserConstants.USERNAME))
           .andExpect(jsonPath("$[0].user.email").value(UserConstants.EMAIL))
           .andExpect(jsonPath("$[0].user.firstName").value(UserConstants.FIRST_NAME))
           .andExpect(jsonPath("$[0].user.lastName").value(UserConstants.LAST_NAME))
           .andExpect(jsonPath("$[0].user.joinDate[0]").value(YEAR))
           .andExpect(jsonPath("$[0].user.joinDate[1]").value(MONTH))
           .andExpect(jsonPath("$[0].user.joinDate[2]").value(DAY));
  }

  @Test
  public void testUpdateReview_returnOldTrue_success() throws Exception {
    when(reviewService.updateReview(any(), anyInt())).thenReturn(ReviewFactory.getDefaultReviewDto());
    String json = objectMapper.writeValueAsString(ReviewFactory.getDefaultReviewRequest());

    mockMvc.perform(put(REVIEWS_ID_PATH, ID)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json)
                      .queryParam(RETURN_OLD, String.valueOf(true)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(ID))
           .andExpect(jsonPath("$.rating").value(RATING))
           .andExpect(jsonPath("$.reviewText").value(REVIEW_TEXT))
           .andExpect(jsonPath("$.dateModified[0]", is(DATE_MODIFIED.getYear())))
           .andExpect(jsonPath("$.dateModified[1]", is(DATE_MODIFIED.getMonthValue())))
           .andExpect(jsonPath("$.dateModified[2]", is(DATE_MODIFIED.getDayOfMonth())))
           .andExpect(jsonPath("$.movie.id").value(MovieConstants.ID))
           .andExpect(jsonPath("$.movie.title").value(MovieConstants.TITLE))
           .andExpect(jsonPath("$.movie.description").value(MovieConstants.DESCRIPTION))
           .andExpect(jsonPath("$.movie.releaseDate[0]", is(MovieConstants.RELEASE_DATE.getYear())))
           .andExpect(jsonPath("$.movie.releaseDate[1]", is(MovieConstants.RELEASE_DATE.getMonthValue())))
           .andExpect(jsonPath("$.movie.releaseDate[2]", is(MovieConstants.RELEASE_DATE.getDayOfMonth())))
           .andExpect(jsonPath("$.cinema").value(CinemaFactory.getDefaultCinemaDto()))
           .andExpect(jsonPath("$.user.id").value(UserConstants.ID))
           .andExpect(jsonPath("$.user.username").value(UserConstants.USERNAME))
           .andExpect(jsonPath("$.user.email").value(UserConstants.EMAIL))
           .andExpect(jsonPath("$.user.firstName").value(UserConstants.FIRST_NAME))
           .andExpect(jsonPath("$.user.lastName").value(UserConstants.LAST_NAME))
           .andExpect(jsonPath("$.user.joinDate[0]").value(YEAR))
           .andExpect(jsonPath("$.user.joinDate[1]").value(MONTH))
           .andExpect(jsonPath("$.user.joinDate[2]").value(DAY));
  }

  @Test
  public void testUpdateReview_returnOldFalse_success() throws Exception {
    when(reviewService.updateReview(any(), anyInt())).thenReturn(ReviewFactory.getDefaultReviewDto());
    String json = objectMapper.writeValueAsString(ReviewFactory.getDefaultReviewRequest());

    mockMvc.perform(put(REVIEWS_ID_PATH, ID)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json)
                      .queryParam(RETURN_OLD, String.valueOf(false)))
           .andExpect(status().isNoContent());
  }

  @Test
  public void testDeleteReview_returnOldTrue_success() throws Exception {
    when(reviewService.deleteReview(anyInt())).thenReturn(ReviewFactory.getDefaultReviewDto());

    mockMvc.perform(delete(REVIEWS_ID_PATH, ID)
                      .queryParam(RETURN_OLD, String.valueOf(true)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(ID))
           .andExpect(jsonPath("$.rating").value(RATING))
           .andExpect(jsonPath("$.reviewText").value(REVIEW_TEXT))
           .andExpect(jsonPath("$.dateModified[0]", is(DATE_MODIFIED.getYear())))
           .andExpect(jsonPath("$.dateModified[1]", is(DATE_MODIFIED.getMonthValue())))
           .andExpect(jsonPath("$.dateModified[2]", is(DATE_MODIFIED.getDayOfMonth())))
           .andExpect(jsonPath("$.movie.id").value(MovieConstants.ID))
           .andExpect(jsonPath("$.movie.title").value(MovieConstants.TITLE))
           .andExpect(jsonPath("$.movie.description").value(MovieConstants.DESCRIPTION))
           .andExpect(jsonPath("$.movie.releaseDate[0]", is(MovieConstants.RELEASE_DATE.getYear())))
           .andExpect(jsonPath("$.movie.releaseDate[1]", is(MovieConstants.RELEASE_DATE.getMonthValue())))
           .andExpect(jsonPath("$.movie.releaseDate[2]", is(MovieConstants.RELEASE_DATE.getDayOfMonth())))
           .andExpect(jsonPath("$.cinema").value(CinemaFactory.getDefaultCinemaDto()))
           .andExpect(jsonPath("$.user.id").value(UserConstants.ID))
           .andExpect(jsonPath("$.user.username").value(UserConstants.USERNAME))
           .andExpect(jsonPath("$.user.email").value(UserConstants.EMAIL))
           .andExpect(jsonPath("$.user.firstName").value(UserConstants.FIRST_NAME))
           .andExpect(jsonPath("$.user.lastName").value(UserConstants.LAST_NAME))
           .andExpect(jsonPath("$.user.joinDate[0]").value(YEAR))
           .andExpect(jsonPath("$.user.joinDate[1]").value(MONTH))
           .andExpect(jsonPath("$.user.joinDate[2]").value(DAY));
  }

  @Test
  public void testDeleteReview_returnOldFalse_success() throws Exception {
    when(reviewService.deleteReview(anyInt())).thenReturn(ReviewFactory.getDefaultReviewDto());

    mockMvc.perform(delete(REVIEWS_ID_PATH, ID)
                      .queryParam(RETURN_OLD, String.valueOf(false)))
           .andExpect(status().isNoContent());
  }
}
