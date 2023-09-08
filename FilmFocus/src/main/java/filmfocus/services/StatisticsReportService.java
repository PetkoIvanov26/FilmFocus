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
public class StatisticsReportService {

  private static final Logger log = LoggerFactory.getLogger(StatisticsReportService.class);

  private final TicketService ticketService;
  private final MovieService movieService;
  private final CategoryService categoryService;

  private final ItemService itemService;
  private final OrderService orderService;

  @Autowired
  public StatisticsReportService(
    TicketService ticketService, MovieService movieService,
    CategoryService categoryService, ItemService itemService, OrderService orderService) {
    this.ticketService = ticketService;
    this.movieService = movieService;
    this.categoryService = categoryService;
    this.itemService = itemService;
    this.orderService = orderService;
  }

  public int getPurchasedTicketsCountByMovieCategory(int categoryId, LocalDate startDate, LocalDate endDate) {
    categoryService.getCategoryById(categoryId);

    List<Ticket> allTickets = ticketService.getTicketsByDateBetween(startDate, endDate);

    log.info("Retrieving all incomes by category ID");

    int totalTickets = 0;
    for (Ticket ticket : allTickets) {
      if (isTicketWithinDateRange(ticket, startDate, endDate) && isTicketForMovieCategory(ticket, categoryId)) {
        totalTickets += 1;
      }
    }

    return totalTickets;
  }

  public int getPurchasedTicketsCountByMovieTitle(String title, LocalDate startDate, LocalDate endDate) {
    movieService.getMovieByTitle(title);
    List<Integer> movieIds = movieService.getIdsOfMoviesByTitle(title);
    List<Ticket> tickets = ticketService.getTicketsByDateBetween(startDate, endDate);

    log.info(
      String.format("Retrieving purchased tickets count for movie with title '%s' between %s and %s", title, startDate,
                    endDate));

    int totalTickets = 0;
    for (Ticket ticket : tickets) {
      if (isTicketWithinDateRange(ticket, startDate, endDate) && isTicketForMovieWithTitle(ticket, movieIds)) {
        totalTickets += 1;
      }
    }

    return totalTickets;
  }

  public int getPurchasedItemsCountByItemName(String name, LocalDate startDate, LocalDate endDate) {
    int totalTickets = 0;

    itemService.getItemDtoByName(name);
    List<Order> allOrders = orderService.getOrdersByDateBetween(startDate, endDate);

    for (Order order : allOrders) {
      if (isOrderWithinDateRange(order, startDate, endDate)) {
        for (Item item : order.getItems()) {
          if (name.equals(item.getName())) {
            totalTickets += 1;
          }
        }
      }
    }

    log.info(
      String.format("Retrieving purchased items count for item with name '%s' between %s and %s",
                    name, startDate, endDate));

    return totalTickets;
  }

  private boolean isOrderWithinDateRange(Order order, LocalDate startDate, LocalDate endDate) {
    LocalDate orderDate = order.getDateOfPurchase();
    return orderDate.isEqual(startDate) || (orderDate.isAfter(startDate) && orderDate.isBefore(endDate));
  }

  private boolean isTicketWithinDateRange(Ticket ticket, LocalDate startDate, LocalDate endDate) {
    LocalDate ticketDate = ticket.getDateOfPurchase();

    return ticketDate.isEqual(startDate) || ticketDate.isEqual(endDate) ||
           (ticketDate.isAfter(startDate) && ticketDate.isBefore(endDate));
  }

  private boolean isTicketForMovieWithTitle(Ticket ticket, List<Integer> movieIds) {
    int movieId = ticket.getProjection().getMovie().getId();

    return movieIds.contains(movieId);
  }

  private boolean isTicketForMovieCategory(Ticket ticket, int id) {
    return id == ticket.getProjection().getMovie().getCategory().getId();
  }
}
