package filmfocus.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import filmfocus.controllers.CategoryController;
import filmfocus.controllers.MovieController;
import filmfocus.controllers.ProjectionController;
import filmfocus.controllers.UserController;
import filmfocus.exceptions.CategoryAlreadyExistsException;
import filmfocus.exceptions.CategoryNotFoundException;
import filmfocus.exceptions.CinemaAlreadyExistsException;
import filmfocus.exceptions.CinemaNotFoundException;
import filmfocus.exceptions.DateNotValidException;
import filmfocus.exceptions.DiscountAlreadyExistsException;
import filmfocus.exceptions.DiscountNotFoundException;
import filmfocus.exceptions.HallNotAvailableException;
import filmfocus.exceptions.DiscountNotValidException;
import filmfocus.exceptions.HallNotFoundException;
import filmfocus.exceptions.ItemAlreadyExistsException;
import filmfocus.exceptions.ItemNotFoundException;
import filmfocus.exceptions.MovieAlreadyExistsException;
import filmfocus.exceptions.MovieNotFoundException;
import filmfocus.exceptions.NoAvailableItemsException;
import filmfocus.exceptions.NoAvailableTicketsException;
import filmfocus.exceptions.NotAuthorizedException;
import filmfocus.exceptions.NotLoggedInException;
import filmfocus.exceptions.OrderNotFoundException;
import filmfocus.exceptions.ProgramAlreadyExistsException;
import filmfocus.exceptions.ProgramNotFoundException;
import filmfocus.exceptions.ProjectionNotFoundException;
import filmfocus.exceptions.ReviewNotFoundException;
import filmfocus.exceptions.RoleAlreadyExistsException;
import filmfocus.exceptions.RoleNotChosenException;
import filmfocus.exceptions.RoleNotFoundException;
import filmfocus.exceptions.TicketNotFoundException;
import filmfocus.exceptions.UserEmailAlreadyExistsException;
import filmfocus.exceptions.UserNotFoundException;
import filmfocus.exceptions.UsernameAlreadyExistsException;
import filmfocus.models.requests.CategoryRequest;
import filmfocus.services.CategoryService;
import filmfocus.services.MovieService;
import filmfocus.services.ProjectionService;
import filmfocus.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.UnexpectedTypeException;
import java.time.format.DateTimeParseException;

import static filmfocus.testUtils.constants.UserConstants.JOIN_DATE;
import static filmfocus.utils.constants.ExceptionMessages.CATEGORY_ALREADY_EXISTS_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.CATEGORY_NOT_FOUND_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.CINEMA_ALREADY_EXISTS_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.CINEMA_NOT_FOUND_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.DATE_NOT_VALID_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.DISCOUNT_ALREADY_EXISTS_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.DISCOUNT_CODE_NOT_VALID_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.DISCOUNT_NOT_FOUND_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.GLOBAL_EXCEPTION;
import static filmfocus.utils.constants.ExceptionMessages.HALL_NOT_AVAILABLE_EXCEPTION;
import static filmfocus.utils.constants.ExceptionMessages.HALL_NOT_FOUND_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.ITEM_ALREADY_EXISTS_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.ITEM_NOT_FOUND_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.MOVIE_ALREADY_EXISTS_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.MOVIE_NOT_FOUND_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.NOT_AUTHORIZED_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.NOT_LOGGED_IN_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.NO_AVAILABLE_ITEMS_EXCEPTION;
import static filmfocus.utils.constants.ExceptionMessages.NO_AVAILABLE_TICKETS_EXCEPTION;
import static filmfocus.utils.constants.ExceptionMessages.ORDER_NOT_FOUND_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.PROGRAM_ALREADY_EXISTS_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.PROGRAM_NOT_FOUND_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.PROJECTION_NOT_FOUND_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.REVIEW_NOT_FOUND_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.ROLE_ALREADY_EXISTS_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.ROLE_NOT_CHOSEN_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.ROLE_NOT_FOUND_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.TICKET_NOT_FOUND_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.USERNAME_ALREADY_EXISTS_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.USER_EMAIL_ALREADY_EXISTS_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.USER_NOT_FOUND_MESSAGE;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
public class GlobalExceptionHandlerTest {

  private final String URI = "/categories";

  private final String ROOT_ERRORS = "$.Errors";

  private MockMvc mockMvc;

  @Mock
  private CategoryService categoryService;

  @InjectMocks
  private CategoryController categoryController;

  @Before
  public void setup() {
    mockMvc = MockMvcBuilders
      .standaloneSetup(categoryController)
      .setControllerAdvice(new GlobalExceptionHandler())
      .build();
  }

  @Test
  public void testHandleMethodArgumentNotValidException_onEndpointGetAllOrders_badRequest() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    String json = objectMapper.writeValueAsString(new CategoryRequest());

    mockMvc.perform(post(URI)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath(ROOT_ERRORS).exists());
  }

  @Test
  public void testHandleInternalAuthenticationServiceException_onEndpointGetAllOrders_badRequest() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(InternalAuthenticationServiceException.class);

    mockMvc.perform(get(URI))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath(ROOT_ERRORS).exists());
  }

  @Test
  public void testHandleBadCredentialsException_onEndpointGetAllOrders_badRequest() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(BadCredentialsException.class);

    mockMvc.perform(get(URI))
           .andExpect(status().isUnauthorized())
           .andExpect(jsonPath(ROOT_ERRORS).exists());
  }

  @Test
  public void testHandleNullPointerException_onEndpointGetAllOrders_badRequest() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(NullPointerException.class);

    mockMvc.perform(get(URI))
           .andExpect(status().isInternalServerError())
           .andExpect(jsonPath(ROOT_ERRORS).exists());
  }

  @Test
  public void testHandleUnexpectedTypeException_onEndpointGetAllOrders_badRequest() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(UnexpectedTypeException.class);

    mockMvc.perform(get(URI))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath(ROOT_ERRORS).exists());
  }

  @Test
  public void testHandleHttpMessageNotReadableException_onEndpointGetAllOrders_badRequest() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(HttpMessageNotReadableException.class);

    mockMvc.perform(get(URI))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath(ROOT_ERRORS).exists());
  }

  @Test
  public void testHandleHttpRequestMethodNotSupportedException_onEndpointGetAllOrders_badRequest() throws Exception {
    mockMvc.perform(patch(URI))
           .andExpect(status().isMethodNotAllowed())
           .andExpect(jsonPath(ROOT_ERRORS).exists());
  }

  @Test
  public void testHandleIllegalArgumentException_onEndpointGetAllOrders_badRequest() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(IllegalArgumentException.class);

    mockMvc.perform(get(URI))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath(ROOT_ERRORS).exists());
  }

  @Test
  public void testHandleMissingServletRequestParameterException_onEndpointGetAllOrders_badRequest() throws Exception {
    MovieService movieService = Mockito.mock(MovieService.class);
    MovieController movieController = new MovieController(movieService);

    MockMvc movieMockMvc =
      MockMvcBuilders.standaloneSetup(movieController).setControllerAdvice(new GlobalExceptionHandler()).build();

    movieMockMvc.perform(MockMvcRequestBuilders.get("/movies"))
                .andExpect(status().isBadRequest());
  }

  @Test
  public void testHandleMissingPathVariableException_onEndpointGetAllMoviesByCategoryId_badRequest() throws Exception {
    MovieService movieService = Mockito.mock(MovieService.class);
    MovieController movieController = new MovieController(movieService);

    MockMvc movieMockMvc =
      MockMvcBuilders.standaloneSetup(movieController).setControllerAdvice(new GlobalExceptionHandler()).build();

    movieMockMvc.perform(MockMvcRequestBuilders.get("/categories/ /movies"))
                .andExpect(status().isBadRequest());
  }

  @Test
  public void testHandleUnsatisfiedServletRequestParameterException_onEndpointGetAllOrders_badRequest() throws
    Exception {
    UserService userService = Mockito.mock(UserService.class);
    UserController userController = new UserController(userService);

    MockMvc movieMockMvc =
      MockMvcBuilders.standaloneSetup(userController).setControllerAdvice(new GlobalExceptionHandler()).build();

    movieMockMvc.perform(MockMvcRequestBuilders.get("/users").queryParam("joinDate", String.valueOf(JOIN_DATE)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ROOT_ERRORS).exists());
  }

  @Test
  public void testHandleDateTimeParseException_onEndpointGetAllOrders_badRequest() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(DateTimeParseException.class);

    mockMvc.perform(get(URI))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath(ROOT_ERRORS).exists());
  }

  @Test
  public void testHandleMethodArgumentTypeMismatchException_onEndpointGetAllOrders_badRequest() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(MethodArgumentTypeMismatchException.class);

    mockMvc.perform(get(URI))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath(ROOT_ERRORS).exists());
  }

  @Test
  public void testHandleDataIntegrityViolationException_onEndpointGetAllOrders_badRequest() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(DataIntegrityViolationException.class);

    mockMvc.perform(get(URI))
           .andExpect(status().isConflict())
           .andExpect(jsonPath(ROOT_ERRORS).exists());
  }

  @Test
  public void testHandleIllegalArgumentException_onEndpointGetAllCategories_badRequest() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(IllegalArgumentException.class);

    mockMvc.perform(get(URI))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath(ROOT_ERRORS).exists());
  }

  @Test
  public void testHandleMissingServletRequestParameterException_onEndpointGetMoviesByReleaseDate_badRequest() throws
    Exception {
    ProjectionService projectionService = Mockito.mock(ProjectionService.class);
    ProjectionController projectionController = new ProjectionController(projectionService);

    MockMvc projectionMockMvc =
      MockMvcBuilders.standaloneSetup(projectionController).setControllerAdvice(new GlobalExceptionHandler()).build();

    projectionMockMvc.perform(get("/projections")
                                .accept(MediaType.APPLICATION_JSON)
                                .param("isBefore", String.valueOf(true)))
                     .andExpect(status().isBadRequest())
                     .andExpect(jsonPath(ROOT_ERRORS).exists());
  }

  @Test
  public void testHandleCategoryAlreadyExistsException_onEndPointGetAllCategories_conflict() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(
      new CategoryAlreadyExistsException(CATEGORY_ALREADY_EXISTS_MESSAGE));

    mockMvc.perform(get(URI))
           .andExpect(status().isConflict())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(CATEGORY_ALREADY_EXISTS_MESSAGE)));
  }

  @Test
  public void testHandleCinemaAlreadyExistsException_onEndPointGetAllCategories_conflict() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(new CinemaAlreadyExistsException(CINEMA_ALREADY_EXISTS_MESSAGE));

    mockMvc.perform(get(URI))
           .andExpect(status().isConflict())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(CINEMA_ALREADY_EXISTS_MESSAGE)));
  }

  @Test
  public void testHandleProgramAlreadyExistsException_onEndPointGetAllCategories_conflict() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(
      new ProgramAlreadyExistsException(PROGRAM_ALREADY_EXISTS_MESSAGE));

    mockMvc.perform(get(URI))
           .andExpect(status().isConflict())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(PROGRAM_ALREADY_EXISTS_MESSAGE)));
  }

  @Test
  public void testHandleRoleAlreadyExistsException_onEndPointGetAllCategories_conflict() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(new RoleAlreadyExistsException(ROLE_ALREADY_EXISTS_MESSAGE));

    mockMvc.perform(get(URI))
           .andExpect(status().isConflict())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(ROLE_ALREADY_EXISTS_MESSAGE)));
  }

  @Test
  public void testHandleUserEmailAlreadyExistsException_onEndPointGetAllCategories_conflict() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(
      new UserEmailAlreadyExistsException(USER_EMAIL_ALREADY_EXISTS_MESSAGE));

    mockMvc.perform(get(URI))
           .andExpect(status().isConflict())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(USER_EMAIL_ALREADY_EXISTS_MESSAGE)));
  }

  @Test
  public void testHandleUsernameAlreadyExistsException_onEndPointGetAllCategories_conflict() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(
      new UsernameAlreadyExistsException(USERNAME_ALREADY_EXISTS_MESSAGE));

    mockMvc.perform(get(URI))
           .andExpect(status().isConflict())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(USERNAME_ALREADY_EXISTS_MESSAGE)));
  }

  @Test
  public void testHandleProjectionNotFoundException_onEndPointGetAllCategories_notFound() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(new ProjectionNotFoundException(PROJECTION_NOT_FOUND_MESSAGE));

    mockMvc.perform(get(URI))
           .andExpect(status().isNotFound())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(PROJECTION_NOT_FOUND_MESSAGE)));
  }

  @Test
  public void testHandleCategoryNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(new CategoryNotFoundException(CATEGORY_NOT_FOUND_MESSAGE));

    mockMvc.perform(get(URI))
           .andExpect(status().isNotFound())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(CATEGORY_NOT_FOUND_MESSAGE)));
  }

  @Test
  public void testHandleCinemaNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(new CinemaNotFoundException(CINEMA_NOT_FOUND_MESSAGE));

    mockMvc.perform(get(URI))
           .andExpect(status().isNotFound())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(CINEMA_NOT_FOUND_MESSAGE)));
  }

  @Test
  public void testHandleProgramNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(new ProgramNotFoundException(PROGRAM_NOT_FOUND_MESSAGE));

    mockMvc.perform(get(URI))
           .andExpect(status().isNotFound())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(PROGRAM_NOT_FOUND_MESSAGE)));
  }

  @Test
  public void testHandleUserNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(new UserNotFoundException(USER_NOT_FOUND_MESSAGE));

    mockMvc.perform(get(URI))
           .andExpect(status().isNotFound())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(USER_NOT_FOUND_MESSAGE)));
  }

  @Test
  public void testHandleDateNotValidException_onEndpointGetAllCategories_notFound() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(new DateNotValidException(DATE_NOT_VALID_MESSAGE));

    mockMvc.perform(get(URI))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(DATE_NOT_VALID_MESSAGE)));
  }

  @Test
  public void testHandleHallNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(new HallNotFoundException(HALL_NOT_FOUND_MESSAGE));

    mockMvc.perform(get(URI))
           .andExpect(status().isNotFound())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(HALL_NOT_FOUND_MESSAGE)));
  }

  @Test
  public void testHandleItemNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(new ItemNotFoundException(ITEM_NOT_FOUND_MESSAGE));

    mockMvc.perform(get(URI))
           .andExpect(status().isNotFound())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(ITEM_NOT_FOUND_MESSAGE)));
  }

  @Test
  public void testHandleExistingItemException_onEndpointGetAllCategories_notFound() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(new ItemAlreadyExistsException(ITEM_ALREADY_EXISTS_MESSAGE));

    mockMvc.perform(get(URI))
           .andExpect(status().isConflict())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(ITEM_ALREADY_EXISTS_MESSAGE)));
  }

  @Test
  public void testHandleExistingMovieException_onEndpointGetAllCategories_notFound() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(new MovieAlreadyExistsException(MOVIE_ALREADY_EXISTS_MESSAGE));

    mockMvc.perform(get(URI))
           .andExpect(status().isConflict())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(MOVIE_ALREADY_EXISTS_MESSAGE)));
  }

  @Test
  public void testHandleOrderNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(new OrderNotFoundException(ORDER_NOT_FOUND_MESSAGE));

    mockMvc.perform(get(URI))
           .andExpect(status().isNotFound())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(ORDER_NOT_FOUND_MESSAGE)));
  }

  @Test
  public void testHandleRoleNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(new RoleNotFoundException(ROLE_NOT_FOUND_MESSAGE));

    mockMvc.perform(get(URI))
           .andExpect(status().isNotFound())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(ROLE_NOT_FOUND_MESSAGE)));
  }

  @Test
  public void testHandleRoleNotChosenException_onEndpointGetAllCategories_notFound() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(new RoleNotChosenException(ROLE_NOT_CHOSEN_MESSAGE));

    mockMvc.perform(get(URI))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(ROLE_NOT_CHOSEN_MESSAGE)));
  }

  @Test
  public void testHandleMovieNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(new MovieNotFoundException(MOVIE_NOT_FOUND_MESSAGE));

    mockMvc.perform(get(URI))
           .andExpect(status().isNotFound())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(MOVIE_NOT_FOUND_MESSAGE)));
  }

  @Test
  public void testHandleDiscountNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(new DiscountNotFoundException(DISCOUNT_NOT_FOUND_MESSAGE));

    mockMvc.perform(get(URI))
           .andExpect(status().isNotFound())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(DISCOUNT_NOT_FOUND_MESSAGE)));
  }

  @Test
  public void testHandeDiscountNotValidException_onEndpointGetAllCategories_notFound() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(new DiscountNotValidException(DISCOUNT_CODE_NOT_VALID_MESSAGE));

    mockMvc.perform(get(URI))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(DISCOUNT_CODE_NOT_VALID_MESSAGE)));
  }

  @Test
  public void testHandleReviewNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(new ReviewNotFoundException(REVIEW_NOT_FOUND_MESSAGE));

    mockMvc.perform(get(URI))
           .andExpect(status().isNotFound())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(REVIEW_NOT_FOUND_MESSAGE)));
  }

  @Test
  public void testHandleTicketNotFoundException_onEndpointGetAllCategories_notFound() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(new TicketNotFoundException(TICKET_NOT_FOUND_MESSAGE));

    mockMvc.perform(get(URI))
           .andExpect(status().isNotFound())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(TICKET_NOT_FOUND_MESSAGE)));
  }

  @Test
  public void testHallNotAvailableException_onEndpointGetAllCategories_notFound() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(new HallNotAvailableException(HALL_NOT_AVAILABLE_EXCEPTION));

    mockMvc.perform(get(URI))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(HALL_NOT_AVAILABLE_EXCEPTION)));
  }

  @Test
  public void testNoAvailableTicketsException_onEndpointGetAllCategories_notFound() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(new NoAvailableTicketsException(NO_AVAILABLE_TICKETS_EXCEPTION));

    mockMvc.perform(get(URI))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(NO_AVAILABLE_TICKETS_EXCEPTION)));
  }

  @Test
  public void testNoAvailableItemsException_onEndpointGetAllCategories_notFound() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(new NoAvailableItemsException(NO_AVAILABLE_ITEMS_EXCEPTION));

    mockMvc.perform(get(URI))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(NO_AVAILABLE_ITEMS_EXCEPTION)));
  }

  @Test
  public void testHandleNotAuthorizedException_onEndpointGetAllCategories_notFound() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(new NotAuthorizedException(NOT_AUTHORIZED_MESSAGE));

    mockMvc.perform(get(URI))
           .andExpect(status().isForbidden())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(NOT_AUTHORIZED_MESSAGE)));
  }

  @Test
  public void testHandleNotLoggedInException_onEndpointGetAllCategories_notFound() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(new NotLoggedInException(NOT_LOGGED_IN_MESSAGE));

    mockMvc.perform(get(URI))
           .andExpect(status().isUnauthorized())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(NOT_LOGGED_IN_MESSAGE)));
  }

  @Test
  public void testHandleDiscountAlreadyExistsException_onEndpointGetAllCategories_conflict() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(
      new DiscountAlreadyExistsException(DISCOUNT_ALREADY_EXISTS_MESSAGE));

    mockMvc.perform(get(URI))
           .andExpect(status().isConflict())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(DISCOUNT_ALREADY_EXISTS_MESSAGE)));
  }

  @Test
  public void testHandleException_onEndpointGetAllCategories_internalServerError() throws Exception {
    when(categoryService.getAllCategories()).thenThrow(IllegalStateException.class);

    mockMvc.perform(get(URI))
           .andExpect(status().isInternalServerError())
           .andExpect(jsonPath(ROOT_ERRORS, containsInAnyOrder(GLOBAL_EXCEPTION)));
  }
}