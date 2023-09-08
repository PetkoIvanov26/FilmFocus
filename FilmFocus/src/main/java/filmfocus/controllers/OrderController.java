package filmfocus.controllers;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import filmfocus.models.dtos.OrderDto;
import filmfocus.models.entities.Order;
import filmfocus.models.requests.OrderRequest;
import filmfocus.models.requests.TicketRequest;
import filmfocus.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static filmfocus.utils.constants.URIConstants.ORDERS_ID_PATH;
import static filmfocus.utils.constants.URIConstants.ORDERS_PATH;
import static filmfocus.utils.constants.URIConstants.USERS_ID_ORDERS_PATH;

@RestController
public class OrderController {

  private static final Logger log = LoggerFactory.getLogger(OrderController.class);

  private final OrderService orderService;

  @Autowired
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping(ORDERS_PATH)
  public ResponseEntity<Void> addOrder(@RequestBody @Valid OrderRequest request) throws MailjetSocketTimeoutException,
    MailjetException {

    Order order = orderService.addOrder(request);
    log.info("A request for an order to be added has been submitted");

    URI location = UriComponentsBuilder
      .fromUriString(ORDERS_ID_PATH)
      .buildAndExpand(order.getId())
      .toUri();

    return ResponseEntity.created(location).build();
  }

  @PostMapping(USERS_ID_ORDERS_PATH)
  public ResponseEntity<Void> makeReservationWithUserId(
    @RequestBody @Valid List<TicketRequest> requests, @PathVariable int id,
    @RequestParam(required = false, defaultValue = "Code") String discountCode) throws
    MailjetSocketTimeoutException, MailjetException {

    Order order = orderService.makeReservationWithUserId(requests, id, discountCode);
    log.info("A request for a ticket to be added has been submitted");

    URI location = UriComponentsBuilder
      .fromUriString(ORDERS_ID_PATH)
      .buildAndExpand(order.getId())
      .toUri();

    return ResponseEntity.created(location).build();
  }

  @GetMapping(USERS_ID_ORDERS_PATH)
  public ResponseEntity<List<OrderDto>> getOrdersByUserId(@PathVariable int id) {
    List<OrderDto> orderDtos = orderService.getOrdersByUserId(id);
    log.info(String.format("All orders by user id %d were requested from the database", id));

    return ResponseEntity.ok(orderDtos);
  }

  @PutMapping(ORDERS_ID_PATH)
  public ResponseEntity<OrderDto> updateOrder(
    @RequestBody @Valid OrderRequest request, @PathVariable int id, @RequestParam(required = false) boolean returnOld) throws
    MailjetSocketTimeoutException, MailjetException {

    OrderDto orderDto = orderService.updateOrder(request, id);
    log.info(String.format("Order with an id %d was updated", id));

    return returnOld ? ResponseEntity.ok(orderDto) : ResponseEntity.noContent().build();
  }

  @DeleteMapping(ORDERS_ID_PATH)
  public ResponseEntity<OrderDto> deleteOrder(@PathVariable int id, @RequestParam(required = false) boolean returnOld) {
    OrderDto orderDto = orderService.deleteOrder(id);
    log.info(String.format("Order with an id %d was deleted", id));

    return returnOld ? ResponseEntity.ok(orderDto) : ResponseEntity.noContent().build();
  }
}