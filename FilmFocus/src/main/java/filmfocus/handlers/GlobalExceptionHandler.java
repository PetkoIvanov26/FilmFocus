package filmfocus.handlers;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.UnexpectedTypeException;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static filmfocus.utils.constants.ExceptionMessages.CAUGHT_EXCEPTION;
import static filmfocus.utils.constants.ExceptionMessages.GLOBAL_EXCEPTION;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, List<String>>> handleIllegalArgumentException(IllegalArgumentException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UnsatisfiedServletRequestParameterException.class)
  public ResponseEntity<Map<String, List<String>>> handleUnsatisfiedServletRequestParameterException(
    UnsatisfiedServletRequestParameterException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NullPointerException.class)
  public ResponseEntity<Map<String, List<String>>> handleNullPointerException(NullPointerException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(UnexpectedTypeException.class)
  public ResponseEntity<Map<String, List<String>>> handleUnexpectedTypeException(UnexpectedTypeException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, List<String>>> handleHttpMessageNotReadableException(
    HttpMessageNotReadableException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<Map<String, List<String>>> handleHttpRequestMethodNotSupportedException(
    HttpRequestMethodNotSupportedException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<Map<String, List<String>>> handleMissingServletRequestParameterException(
    MissingServletRequestParameterException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MissingPathVariableException.class)
  public ResponseEntity<Map<String, List<String>>> handleMissingPathVariableException(
    MissingPathVariableException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(DateTimeParseException.class)
  public ResponseEntity<Map<String, List<String>>> handleDateTimeParseException(DateTimeParseException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, List<String>>> handleControllerValidationException(
    MethodArgumentNotValidException exception) {

    log.error("Caught exception: ", exception);

    List<String> errors = exception.getBindingResult().getFieldErrors().stream()
                                   .map(FieldError::getDefaultMessage)
                                   .collect(Collectors.toList());
    return new ResponseEntity<>(formatErrorsResponse(errors), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Map<String, List<String>>> handleMethodArgumentTypeMismatchException(
    MethodArgumentTypeMismatchException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InternalAuthenticationServiceException.class)
  public ResponseEntity<Map<String, List<String>>> handleInternalAuthenticationServiceException(
    InternalAuthenticationServiceException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(RoleNotChosenException.class)
  public ResponseEntity<Map<String, List<String>>> handleRoleNotChosenException(RoleNotChosenException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<Map<String, List<String>>> handleBadCredentialsException(BadCredentialsException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Map<String, List<String>>> handleDataIntegrityViolationException(
    DataIntegrityViolationException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(DateNotValidException.class)
  public ResponseEntity<Map<String, List<String>>> handleDateNotValidException(DateNotValidException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotLoggedInException.class)
  public ResponseEntity<Map<String, List<String>>> handleNotLoggedInException(NotLoggedInException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(NotAuthorizedException.class)
  public ResponseEntity<Map<String, List<String>>> handleNotAuthorizedException(NotAuthorizedException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(ProjectionNotFoundException.class)
  public ResponseEntity<Map<String, List<String>>> handleProjectionNotFoundException(
    ProjectionNotFoundException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());
    return new ResponseEntity<>(errorsMap, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(CategoryNotFoundException.class)
  public ResponseEntity<Map<String, List<String>>> handleCategoryNotFoundException(
    CategoryNotFoundException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(CinemaNotFoundException.class)
  public ResponseEntity<Map<String, List<String>>> handleCinemaNotFoundException(CinemaNotFoundException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ProgramNotFoundException.class)
  public ResponseEntity<Map<String, List<String>>> handleProgramNotFoundException(ProgramNotFoundException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<Map<String, List<String>>> handleUserNotFoundException(UserNotFoundException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(HallNotFoundException.class)
  public ResponseEntity<Map<String, List<String>>> handleHallNotFoundException(HallNotFoundException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ItemNotFoundException.class)
  public ResponseEntity<Map<String, List<String>>> handleItemNotFoundException(ItemNotFoundException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(OrderNotFoundException.class)
  public ResponseEntity<Map<String, List<String>>> handleOrderNotFoundException(OrderNotFoundException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());
    return new ResponseEntity<>(errorsMap, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MovieNotFoundException.class)
  public ResponseEntity<Map<String, List<String>>> handleMovieNotFoundException(MovieNotFoundException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(RoleNotFoundException.class)
  public ResponseEntity<Map<String, List<String>>> handleRoleNotFoundException(RoleNotFoundException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(TicketNotFoundException.class)
  public ResponseEntity<Map<String, List<String>>> handleTicketNotFoundException(TicketNotFoundException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(DiscountNotFoundException.class)
  public ResponseEntity<Map<String, List<String>>> handleDiscountNotFoundException(
    DiscountNotFoundException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ReviewNotFoundException.class)
  public ResponseEntity<Map<String, List<String>>> handleReviewNotFoundException(
    ReviewNotFoundException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(HallNotAvailableException.class)
  public ResponseEntity<Map<String, List<String>>> handleHallNotAvailableException(
    HallNotAvailableException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NoAvailableTicketsException.class)
  public ResponseEntity<Map<String, List<String>>> handleNoAvailableTicketsException(
    NoAvailableTicketsException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NoAvailableItemsException.class)
  public ResponseEntity<Map<String, List<String>>> handleNoAvailableItemsException(
    NoAvailableItemsException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(CategoryAlreadyExistsException.class)
  public ResponseEntity<Map<String, List<String>>> handleCategoryAlreadyExistsException(
    CategoryAlreadyExistsException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());
    return new ResponseEntity<>(errorsMap, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(CinemaAlreadyExistsException.class)
  public ResponseEntity<Map<String, List<String>>> handleCinemaAlreadyExistsException(
    CinemaAlreadyExistsException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());
    return new ResponseEntity<>(errorsMap, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(ProgramAlreadyExistsException.class)
  public ResponseEntity<Map<String, List<String>>> handleProgramAlreadyExistsException(
    ProgramAlreadyExistsException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());
    return new ResponseEntity<>(errorsMap, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(RoleAlreadyExistsException.class)
  public ResponseEntity<Map<String, List<String>>> handleRoleAlreadyExistsException(
    RoleAlreadyExistsException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());
    return new ResponseEntity<>(errorsMap, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(UserEmailAlreadyExistsException.class)
  public ResponseEntity<Map<String, List<String>>> handleUserEmailAlreadyExistsException(
    UserEmailAlreadyExistsException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());
    return new ResponseEntity<>(errorsMap, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(UsernameAlreadyExistsException.class)
  public ResponseEntity<Map<String, List<String>>> handleUsernameAlreadyExistsException(
    UsernameAlreadyExistsException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());
    return new ResponseEntity<>(errorsMap, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(ItemAlreadyExistsException.class)
  public ResponseEntity<Map<String, List<String>>> handleExistingItemException(ItemAlreadyExistsException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(MovieAlreadyExistsException.class)
  public ResponseEntity<Map<String, List<String>>> handleExistingMovieException(MovieAlreadyExistsException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(DiscountNotValidException.class)
  public ResponseEntity<Map<String, List<String>>> handleDiscountNotValidException(
    DiscountNotValidException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(DiscountAlreadyExistsException.class)
  public ResponseEntity<Map<String, List<String>>> handleDiscountAlreadyExistsException(
    DiscountAlreadyExistsException exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(exception.getMessage());

    return new ResponseEntity<>(errorsMap, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, List<String>>> handleException(Exception exception) {
    log.error(CAUGHT_EXCEPTION, exception);

    Map<String, List<String>> errorsMap = formatErrorsResponse(GLOBAL_EXCEPTION);

    return new ResponseEntity<>(errorsMap, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private Map<String, List<String>> formatErrorsResponse(String... errors) {
    return formatErrorsResponse(Arrays.stream(errors).collect(Collectors.toList()));
  }

  private Map<String, List<String>> formatErrorsResponse(List<String> errors) {
    Map<String, List<String>> errorResponse = new HashMap<>(4);
    errorResponse.put("Errors", errors);
    return errorResponse;
  }
}
