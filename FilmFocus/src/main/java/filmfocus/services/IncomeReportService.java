package filmfocus.services;

import filmfocus.models.entities.Item;
import filmfocus.models.entities.Order;
import filmfocus.models.entities.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class IncomeReportService {

  private static final Logger log = LoggerFactory.getLogger(IncomeReportService.class);

  private final CinemaService cinemaService;
  private final HallService hallService;
  private final ItemService itemService;
  private final MovieService movieService;
  private final UserService userService;
  private final OrderService orderService;
  private final TicketService ticketService;

  @Autowired
  public IncomeReportService(
    CinemaService cinemaService, HallService hallService, ItemService itemService,
    MovieService movieService, UserService userService, OrderService orderService, TicketService ticketService) {
    this.cinemaService = cinemaService;
    this.hallService = hallService;
    this.itemService = itemService;
    this.movieService = movieService;
    this.userService = userService;
    this.orderService = orderService;
    this.ticketService = ticketService;
  }

  public double getAllIncomesByCinemaId(int id, LocalDate startDate, LocalDate endDate) {
    double incomes = 0;

    cinemaService.getCinemaById(id);

    List<Order> allOrders = orderService.getOrdersByDateBetween(startDate, endDate);

    for (Order order : allOrders) {
      if (isOrderWithinDateRange(order, startDate, endDate) && isOrderFromCinema(order, id)) {
        incomes += order.getTotalPrice();
      }
    }

    log.info(String.format("All incomes by cinema id %d calculated", id));

    return incomes;
  }

  public double getAllIncomesByHallId(int id, LocalDate startDate, LocalDate endDate) {
    double incomes = 0;

    hallService.getHallById(id);

    List<Order> allOrders = orderService.getOrdersByDateBetween(startDate, endDate);

    for (Order order : allOrders) {
      if (isOrderWithinDateRange(order, startDate, endDate) && isOrderFromHall(order, id)) {
        incomes += order.getTotalPrice();
      }
    }

    log.info(String.format("All incomes by hall id %d calculated", id));

    return incomes;
  }

  public double getAllIncomesByItemId(int id, LocalDate startDate, LocalDate endDate) {
    double incomes = 0;

    itemService.getItemDtoById(id);

    List<Order> allOrders = orderService.getOrdersByDateBetween(startDate, endDate);

    for (Order order : allOrders) {
      if (isOrderWithinDateRange(order, startDate, endDate)) {
        for (Item item : order.getItems()) {
          if (id == item.getId()) {
            incomes += item.getPrice();
          }
        }
      }
    }

    log.info(String.format("All incomes by item id %d calculated", id));

    return incomes;
  }

  public double getAllIncomesByMovieId(int id, LocalDate startDate, LocalDate endDate) {
    double incomes = 0;

    movieService.getMovieById(id);

    List<Ticket> allTickets = ticketService.getTicketsByDateBetween(startDate, endDate);

    for (Ticket ticket : allTickets) {
      if (isTicketWithinDateRange(ticket, startDate, endDate) && isTicketForMovie(ticket, id)) {
        incomes += ticket.getProjection().getPrice();
      }
    }

    log.info(String.format("All incomes by movie id %d calculated", id));

    return incomes;
  }

  public double getAllIncomesByUserId(int id, LocalDate startDate, LocalDate endDate) {
    double incomes = 0;

    userService.getUserById(id);

    List<Order> allOrders = orderService.getOrdersByDateBetween(startDate, endDate);

    for (Order order : allOrders) {
      if (isOrderWithinDateRange(order, startDate, endDate) && isOrderFromUser(order, id)) {
        incomes += order.getTotalPrice();
      }
    }

    log.info(String.format("All incomes by user id %d calculated", id));

    return incomes;
  }

  private boolean isOrderFromCinema(Order order, int cinemaId) {
    List<Ticket> tickets = order.getTickets();

    for (Ticket ticket : tickets) {
      if (cinemaId == ticket.getProjection().getHall().getCinema().getId()) {

        log.info("Order is from cinema");

        return true;
      }
    }

    log.info("Order is not from cinema");

    return false;
  }

  private boolean isOrderFromHall(Order order, int hallId) {
    List<Ticket> tickets = order.getTickets();

    for (Ticket ticket : tickets) {
      if (hallId == ticket.getProjection().getHall().getId()) {

        log.info("Order is from hall");

        return true;
      }
    }

    log.info("Order is not from hall");

    return false;
  }

  private boolean isTicketForMovie(Ticket ticket, int movieId) {
    return movieId == ticket.getProjection().getMovie().getId();
  }

  private boolean isOrderFromUser(Order order, int userId) {
    return userId == order.getUser().getId();
  }

  private boolean isTicketWithinDateRange(Ticket ticket, LocalDate startDate, LocalDate endDate) {
    LocalDate ticketDate = ticket.getDateOfPurchase();

    return ticketDate.isEqual(startDate) || ticketDate.isEqual(endDate) ||
           (ticketDate.isAfter(startDate) && ticketDate.isBefore(endDate));
  }

  private boolean isOrderWithinDateRange(Order order, LocalDate startDate, LocalDate endDate) {
    LocalDate orderDate = order.getDateOfPurchase();

    return orderDate.isEqual(startDate) || (orderDate.isAfter(startDate) && orderDate.isBefore(endDate));
  }
}
