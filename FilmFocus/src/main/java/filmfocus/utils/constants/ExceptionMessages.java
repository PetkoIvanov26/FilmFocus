package filmfocus.utils.constants;

public final class ExceptionMessages {

  /**
   * Not found messages
   */

  public static final String CATEGORY_NOT_FOUND_MESSAGE = "No such category was found in the database!";
  public static final String CINEMA_NOT_FOUND_MESSAGE = "No such cinema was found in the database!";
  public static final String PROGRAM_NOT_FOUND_MESSAGE = "No such program was found in the database!";
  public static final String USER_NOT_FOUND_MESSAGE = "No such user was found in the database!";
  public static final String ITEM_NOT_FOUND_MESSAGE = "No such item was found in the database!";
  public static final String HALL_NOT_FOUND_MESSAGE = "No such hall was found in the database!";
  public static final String PROJECTION_NOT_FOUND_MESSAGE = "No such projection was found in the database!";
  public static final String MOVIE_NOT_FOUND_MESSAGE = "No such movie was found in the database!";
  public static final String ORDER_NOT_FOUND_MESSAGE = "No such order was found in the database!";
  public static final String ROLE_NOT_FOUND_MESSAGE = "No such role was found in the database!";
  public static final String ROLE_NOT_CHOSEN_MESSAGE = "You haven't specified any roles!";
  public static final String REVIEW_NOT_FOUND_MESSAGE = "No such review was found in the database!";
  public static final String TICKET_NOT_FOUND_MESSAGE = "No such ticket was found in the database!";
  public static final String DISCOUNT_NOT_FOUND_MESSAGE = "No such discount was found in the database!";

  /**
   * Authorization messages
   */

  public static final String NOT_AUTHORIZED_MESSAGE = "You are not authorized to perform this action!";
  public static final String NOT_LOGGED_IN_MESSAGE = "You are not logged in!";

  /**
   * Validation messages
   */

  public static final String DATE_NOT_VALID_MESSAGE = "Date not valid.";
  public static final String HALL_NOT_AVAILABLE_EXCEPTION = "The hall is not available for the desired start time.";
  public static final String DISCOUNT_CODE_NOT_VALID_MESSAGE = "Discount code not valid.";
  public static final String NO_AVAILABLE_TICKETS_EXCEPTION = "No more tickets available for this projection.";
  public static final String NO_AVAILABLE_ITEMS_EXCEPTION = "No more items of this kind available.";

  /**
   * Existing entity properties messages
   */

  public static final String CATEGORY_ALREADY_EXISTS_MESSAGE = "Category already exists!";
  public static final String CINEMA_ALREADY_EXISTS_MESSAGE = "Cinema already exists!";
  public static final String DISCOUNT_ALREADY_EXISTS_MESSAGE = "Discount already exists!";
  public static final String ITEM_ALREADY_EXISTS_MESSAGE = "Item already exists!";
  public static final String PROGRAM_ALREADY_EXISTS_MESSAGE = "Program already exists!";
  public static final String MOVIE_ALREADY_EXISTS_MESSAGE = "Movie already exists!";
  public static final String USERNAME_ALREADY_EXISTS_MESSAGE = "Username already exists!";
  public static final String USER_EMAIL_ALREADY_EXISTS_MESSAGE = "Email already exists!";
  public static final String ROLE_ALREADY_EXISTS_MESSAGE = "Role already exists!";

  /**
   * Message for constructor in non-instantiable classes
   */

  public static final String NON_INSTANTIABLE_CLASS_MESSAGE = "Do not instantiate this class!";

  /**
   * Global exception messages
   */

  public static final String GLOBAL_EXCEPTION = "Something went wrong...";
  public final static String CAUGHT_EXCEPTION = "An exception has been caught";

  private ExceptionMessages() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }
}
