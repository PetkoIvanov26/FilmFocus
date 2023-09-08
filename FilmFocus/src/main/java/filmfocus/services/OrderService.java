package filmfocus.services;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import filmfocus.exceptions.DiscountNotFoundException;
import filmfocus.exceptions.DiscountNotValidException;
import filmfocus.exceptions.NotAuthorizedException;
import filmfocus.exceptions.OrderNotFoundException;
import filmfocus.mappers.OrderMapper;
import filmfocus.models.dtos.OrderDto;
import filmfocus.models.entities.Item;
import filmfocus.models.entities.Order;
import filmfocus.models.entities.Ticket;
import filmfocus.models.entities.User;
import filmfocus.models.requests.OrderRequest;
import filmfocus.models.requests.TicketRequest;
import filmfocus.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static filmfocus.utils.constants.ExceptionMessages.DISCOUNT_CODE_NOT_VALID_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.DISCOUNT_NOT_FOUND_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.NOT_AUTHORIZED_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.ORDER_NOT_FOUND_MESSAGE;

@Service
public class OrderService {

  private final Logger log = LoggerFactory.getLogger(OrderService.class);
  private final DiscountService discountService;
  private final OrderMapper orderMapper;
  private final OrderRepository orderRepository;
  private final UserService userService;
  private final TicketService ticketService;
  private final ItemService itemService;
  private final EmailService emailService;

  @Autowired
  public OrderService(
    DiscountService discountService, OrderMapper orderMapper, OrderRepository orderRepository,
    UserService userService, TicketService ticketService, ItemService itemService, EmailService emailService) {
    this.discountService = discountService;
    this.orderMapper = orderMapper;
    this.orderRepository = orderRepository;
    this.userService = userService;
    this.ticketService = ticketService;
    this.itemService = itemService;
    this.emailService = emailService;
  }

  public Order addOrder(OrderRequest request) throws MailjetSocketTimeoutException, MailjetException {
    List<Ticket> tickets =
      request.getTicketsIds().stream().map(ticketService::getTicketById).collect(Collectors.toList());

    List<Item> items = new ArrayList<>();

    if (Objects.nonNull(request.getItemsIds()) && !request.getItemsIds().isEmpty()) {
      items = request.getItemsIds().stream()
                     .map(itemService::getItemById)
                     .collect(Collectors.toList());

      for (Item item : items) {
        itemService.decrementItemQuantity(item);
      }
    }

    User user = userService.getUserById(request.getUserId());

    double price = calculateOrderPrice(items, tickets);

    String discountCode = request.getDiscountCode();
    if (Objects.nonNull(discountCode) && isDiscountCodeValid(discountCode)) {
      price = discountService.applyDiscount(price, discountCode);
    }

    log.info("An attempt to add a new order in the database");

    Order order = new Order(LocalDate.now(), user, tickets, items, price);

    order = orderRepository.save(order);

    emailService.sendOrderConfirmationEmail(user, order);

    return order;
  }

  public Order makeReservationWithUserId(List<TicketRequest> requests, int userId, String discountCode) throws
    MailjetSocketTimeoutException,
    MailjetException {
    List<Item> items = Collections.emptyList();
    List<Ticket> tickets = new ArrayList<>();

    User user = userService.getUserById(userId);

    if (!userService.isCurrentUserAuthorized(userId)) {
      log.error(String.format("Exception caught: %s", NOT_AUTHORIZED_MESSAGE));
      throw new NotAuthorizedException(NOT_AUTHORIZED_MESSAGE);
    }

    for (TicketRequest request : requests) {
      tickets.add(ticketService.addTicket(new TicketRequest(request.getProjectionId())));
    }

    double price = calculateOrderPrice(items, tickets);

    if (Objects.nonNull(discountCode) && isDiscountCodeValid(discountCode)) {
      price = discountService.applyDiscount(price, discountCode);
    }

    log.info("An attempt to add a new order in the database");

    Order order = orderRepository.save(new Order(LocalDate.now(), user, tickets, items, price));

    emailService.sendOrderConfirmationEmail(user, order);

    return order;
  }

  public List<OrderDto> getOrdersByUserId(int userId) {
    User currentUser = userService.getCurrentUser();

    boolean isAdminOrVendor = currentUser
      .getRoles().stream()
      .anyMatch(role -> role.getName().equals("ADMIN") || role.getName().equals("VENDOR"));

    if (!(isAdminOrVendor || currentUser.getId() == userId)) {
      log.error(String.format("Exception caught: %s", NOT_AUTHORIZED_MESSAGE));

      throw new NotAuthorizedException(NOT_AUTHORIZED_MESSAGE);
    }

    log.info(String.format("All orders with user id %d were requested from the database", userId));

    return this.orderMapper.mapOrderToOrderDtoList(orderRepository.findOrderByUserId(userId));
  }

  public List<Order> getOrdersByDateBetween(LocalDate startDate, LocalDate endDate) {
    log.info(String.format("All orders with date between %s and %s were requested from the database", startDate,
                           endDate));
    return orderRepository.findOrdersByDateOfPurchaseBetween(startDate, endDate);
  }

  public OrderDto updateOrder(OrderRequest request, int id) throws MailjetSocketTimeoutException, MailjetException {
    Order order = orderRepository.findById(id).orElseThrow(() -> {
      log.error(String.format("Exception caught: %s", ORDER_NOT_FOUND_MESSAGE));

      throw new OrderNotFoundException(ORDER_NOT_FOUND_MESSAGE);
    });

    OrderDto orderDto = orderMapper.mapOrderToOrderDto(order);

    List<Ticket> tickets =
      request.getTicketsIds().stream().map(ticketService::getTicketById).collect(Collectors.toList());

    List<Item> items = new ArrayList<>();
    if (Objects.nonNull(request.getItemsIds()) && !request.getItemsIds().isEmpty()) {
      items = request.getItemsIds().stream()
                     .map(itemService::getItemById)
                     .collect(Collectors.toList());
    }

    User user = userService.getUserById(request.getUserId());

    double price = calculateOrderPrice(items, tickets);

    if (!items.isEmpty()) {
      List<Item> existingItems = new ArrayList<>(order.getItems());

      for (Item item : items) {
        if (!existingItems.remove(item)) {
          itemService.decrementItemQuantity(item);
        }
      }

      for (Item removedItem : existingItems) {
        itemService.incrementItemQuantity(removedItem);
      }
    } else {
      for (Item previousItem : order.getItems()) {
        itemService.incrementItemQuantity(previousItem);
      }
    }


    order.setItems(items);
    order.setTickets(tickets);
    order.setUser(user);
    order.setTotalPrice(price);

    orderRepository.save(order);

    log.info(String.format("Order with id %d was updated", id));

    emailService.sendOrderConfirmationEmail(user, order);

    return orderDto;
  }

  public OrderDto deleteOrder(int id) {
    Order order = orderRepository.findById(id).orElseThrow(() -> {
      log.error(String.format("Exception caught: %s", ORDER_NOT_FOUND_MESSAGE));
      throw new OrderNotFoundException(ORDER_NOT_FOUND_MESSAGE);
    });

    OrderDto orderDto = orderMapper.mapOrderToOrderDto(order);

    orderRepository.delete(order);
    log.info(String.format("Order with id %d was deleted from the database", id));
    return orderDto;
  }

  private boolean isDiscountCodeValid(String discountCode) {
    String regex = "\\d{4}";

    if (discountCode.matches(regex)) {
      return true;
    } else {
      throw new DiscountNotValidException(DISCOUNT_CODE_NOT_VALID_MESSAGE);
    }
  }

  private double calculateOrderPrice(List<Item> items, List<Ticket> tickets) {
    double sum = 0;

    if (Objects.nonNull(items) && items.size() > 0) {
      for (Item item : items) {
        sum += item.getPrice();
      }
    }

    if (Objects.nonNull(tickets) && tickets.size() > 0) {
      for (Ticket ticket : tickets) {
        sum += ticket.getProjection().getPrice();
      }
    }
    return sum;
  }
}