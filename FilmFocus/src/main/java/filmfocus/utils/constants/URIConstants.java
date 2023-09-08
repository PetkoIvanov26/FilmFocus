package filmfocus.utils.constants;

import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class URIConstants {

  /**
   * Categories endpoints
   */

  public static final String CATEGORIES_PATH = "/categories";
  public static final String CATEGORIES_ID_PATH = "/categories/{id}";

  /**
   * Cinemas endpoints
   */

  public static final String CINEMAS_PATH = "/cinemas";
  public static final String CINEMAS_ID_PATH = "/cinemas/{id}";

  /**
   * Discounts endpoints
   */

  public static final String DISCOUNTS_PATH = "/discounts";
  public static final String DISCOUNTS_ID_PATH = "/discounts/{id}";

  /**
   * Halls endpoints
   */

  public static final String HALLS_PATH = "/halls";
  public static final String HALLS_ID_PATH = "/halls/{id}";
  public static final String CINEMAS_ID_HALLS_PATH = "/cinemas/{id}/halls";

  /**
   * Reports endpoints
   */

  public static final String REPORTS_CINEMAS_ID_INCOMES_PATH = "/reports/cinemas/{id}/incomes";
  public static final String REPORTS_HALLS_ID_INCOMES_PATH = "/reports/halls/{id}/incomes";
  public static final String REPORTS_ITEMS_ID_INCOMES_PATH = "/reports/items/{id}/incomes";
  public static final String REPORTS_MOVIES_ID_INCOMES_PATH = "/reports/movies/{id}/incomes";
  public static final String REPORTS_USERS_ID_INCOMES_PATH = "/reports/users/{id}/incomes";

  /**
   * Items endpoints
   */

  public static final String ITEMS_PATH = "/items";
  public static final String ITEMS_ID_PATH = "/items/{id}";

  /**
   * Movies endpoints
   */

  public static final String MOVIES_PATH = "/movies";
  public static final String MOVIES_ID_PATH = "/movies/{id}";
  public static final String CATEGORIES_ID_MOVIES_PATH = "/categories/{id}/movies";

  /**
   * Orders endpoints
   */

  public static final String ORDERS_PATH = "/orders";
  public static final String ORDERS_ID_PATH = "/orders/{id}";
  public static final String USERS_ID_ORDERS_PATH = "/users/{id}/orders";

  /**
   * Programs endpoints
   */

  public static final String PROGRAMS_PATH = "/programs";
  public static final String PROGRAMS_ID_PATH = "/programs/{id}";
  public static final String CINEMAS_ID_PROGRAMS_PATH = "/cinemas/{id}/programs";

  /**
   * Projections endpoints
   */

  public static final String PROJECTIONS_PATH = "/projections";
  public static final String PROJECTIONS_ID_PATH = "/projections/{id}";
  public static final String PROGRAMS_ID_PROJECTIONS_PATH = "/programs/{id}/projections";
  public static final String MOVIES_ID_PROJECTIONS_PATH = "/movies/{id}/projections";

  /**
   * Reviews endpoints
   */

  public static final String REVIEWS_ID_PATH = "/reviews/{id}";
  public static final String CINEMAS_ID_REVIEWS_PATH = "/cinemas/{id}/reviews";
  public static final String MOVIES_ID_REVIEWS_PATH = "/movies/{id}/reviews";
  public static final String USERS_ID_MOVIES_REVIEWS_PATH = "/users/{id}/movies/reviews";
  public static final String USERS_ID_CINEMAS_REVIEWS_PATH = "/users/{id}/cinemas/reviews";

  /**
   * Roles endpoints
   */

  public static final String ROLES_PATH = "/roles";
  public static final String ROLES_ID_PATH = "/roles/{id}";

  /**
   * Reports endpoints
   */

  public static final String REPORTS_MOVIES_CATEGORIES_ID_TICKETS_COUNT_PATH =
    "/reports/movies/categories/{id}/tickets-count";
  public static final String REPORTS_MOVIES_TICKETS_COUNT_PATH = "/reports/movies/tickets-count";
  public static final String REPORTS_ITEMS_ITEMS_COUNT_PATH = "/reports/items/items-count";

  /**
   * Tickets endpoints
   */

  public static final String TICKETS_PATH = "/tickets";
  public static final String TICKETS_ID_PATH = "/tickets/{id}";
  public static final String PROJECTIONS_ID_TICKETS_PATH = "/projections/{id}/tickets";

  /**
   * Users endpoints
   */

  public static final String USERS_PATH = "/users";
  public static final String USERS_ID_PATH = "/users/{id}";
  public static final String ADMINS_PATH = "/admins";
  public static final String ADMINS_ID_PATH = "/admins/{id}";
  public static final String LOGIN_PATH = "/login";
  public static final String REGISTRATION_PATH = "/registration";

  private URIConstants() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }
}
