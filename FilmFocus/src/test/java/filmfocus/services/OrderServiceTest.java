package filmfocus.services;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
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
import filmfocus.repositories.OrderRepository;
import filmfocus.testUtils.factories.ItemFactory;
import filmfocus.testUtils.factories.OrderFactory;
import filmfocus.testUtils.factories.ProjectionFactory;
import filmfocus.testUtils.factories.TicketFactory;
import filmfocus.testUtils.factories.UserFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static filmfocus.testUtils.constants.DiscountConstants.CODE;
import static filmfocus.testUtils.constants.OrderConstants.ID;
import static filmfocus.testUtils.constants.OrderConstants.TOTAL_PRICE;
import static filmfocus.testUtils.constants.ReportConstants.END_DATE;
import static filmfocus.testUtils.constants.ReportConstants.START_DATE;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

  @Mock
  private OrderMapper orderMapper;

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private UserService userService;

  @Mock
  private TicketService ticketService;

  @Mock
  private ItemService itemService;

  @Mock
  private EmailService emailService;

  @Mock
  private DiscountService discountService;

  @InjectMocks
  private OrderService orderService;

  @Test
  public void testAddOrder_noExceptions_success() throws MailjetSocketTimeoutException, MailjetException {
    Order expected = OrderFactory.getDefaultOrder();

    when(ticketService.getTicketById(anyInt())).thenReturn(TicketFactory.getDefaultTicket());
    when(itemService.getItemById(anyInt())).thenReturn(ItemFactory.getDefaultItem());
    when(userService.getUserById(anyInt())).thenReturn(UserFactory.getDefaultUser());
    when(orderRepository.save(any())).thenReturn(OrderFactory.getDefaultOrder());
    when(discountService.applyDiscount(anyDouble(), anyString())).thenReturn(TOTAL_PRICE);

    Order order = orderService.addOrder(OrderFactory.getDefaultOrderRequest());

    assertEquals(expected, order);
    verify(emailService).sendOrderConfirmationEmail(any(), any());
  }

  @Test(expected = DiscountNotValidException.class)
  public void testAddOrder_discountCodeNotValid_throwsDiscountNotFoundException() throws MailjetSocketTimeoutException,
    MailjetException {
    when(ticketService.getTicketById(anyInt())).thenReturn(TicketFactory.getDefaultTicket());
    when(itemService.getItemById(anyInt())).thenReturn(ItemFactory.getDefaultItem());
    when(userService.getUserById(anyInt())).thenReturn(UserFactory.getDefaultUser());

    OrderRequest orderRequest = OrderFactory.getDefaultOrderRequest();
    orderRequest.setDiscountCode("Code");
    orderService.addOrder(orderRequest);
  }

  @Test
  public void testMakeReservationWithUserId_noExceptions_success() throws MailjetSocketTimeoutException,
    MailjetException {
    Order expected = OrderFactory.getDefaultOrder();
    Ticket ticket = new Ticket();
    ticket.setId(ID);
    ticket.setDateOfPurchase(LocalDate.now());
    ticket.setProjection(ProjectionFactory.getDefaultProjection());

    when(ticketService.addTicket(any())).thenReturn(ticket);
    when(userService.getUserById(anyInt())).thenReturn(UserFactory.getDefaultUser());
    when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(true);
    when(orderRepository.save(any())).thenReturn(expected);
    when(discountService.applyDiscount(anyDouble(), anyString())).thenReturn(TOTAL_PRICE);

    Order order =
      orderService.makeReservationWithUserId(Collections.singletonList(TicketFactory.getDefaultTicketRequest()), ID,
                                             CODE);

    assertEquals(expected, order);
  }

  @Test(expected = DiscountNotValidException.class)
  public void testMakeReservationWithUserId_discountCodeNotValid_throwsDiscountNotFoundException() throws
    MailjetSocketTimeoutException,
    MailjetException {
    Ticket ticket = new Ticket();
    ticket.setId(ID);
    ticket.setDateOfPurchase(LocalDate.now());
    ticket.setProjection(ProjectionFactory.getDefaultProjection());

    String invalidDiscount = "ABC";

    when(ticketService.addTicket(any())).thenReturn(ticket);
    when(userService.getUserById(anyInt())).thenReturn(UserFactory.getDefaultUser());
    when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(true);

    orderService.makeReservationWithUserId(Collections.singletonList(TicketFactory.getDefaultTicketRequest()), ID,
                                           invalidDiscount);
  }

  @Test(expected = NotAuthorizedException.class)
  public void testMakeReservationWithUserId_invalidUserId_throwsNotAuthorizedException() throws
    MailjetSocketTimeoutException, MailjetException {
    when(userService.isCurrentUserAuthorized(anyInt())).thenReturn(false);

    orderService.makeReservationWithUserId(Collections.singletonList(TicketFactory.getDefaultTicketRequest()), ID,
                                           null);
  }

  @Test
  public void testGetOrdersByUserId_noExceptions_success() {
    User user = UserFactory.getDefaultUser();

    when(userService.getCurrentUser()).thenReturn(user);
    when(orderRepository.findOrderByUserId(anyInt())).thenReturn(OrderFactory.getDefaultOrderList());

    List<OrderDto> orderDtos = OrderFactory.getDefaultOrderDtoList();
    when(orderMapper.mapOrderToOrderDtoList(OrderFactory.getDefaultOrderList())).thenReturn(orderDtos);
    when(orderRepository.findOrderByUserId(anyInt())).thenReturn(OrderFactory.getDefaultOrderList());

    List<OrderDto> result = orderService.getOrdersByUserId(ID);

    assertEquals(orderDtos, result);
  }

  @Test(expected = NotAuthorizedException.class)
  public void testGetOrdersByUserId_invalidUserId_throwsNotAuthorizedException() {
    User user = UserFactory.getDefaultUser();

    when(userService.getCurrentUser()).thenReturn(user);

    int invalidUserId = 999;

    orderService.getOrdersByUserId(invalidUserId);
  }

  @Test
  public void testUpdateOrder_noExceptions_success() throws MailjetSocketTimeoutException, MailjetException {
    OrderDto expected = OrderFactory.getDefaultOrderDto();

    when(orderRepository.findById(anyInt())).thenReturn(Optional.of(OrderFactory.getDefaultOrder()));
    when(orderMapper.mapOrderToOrderDto(any())).thenReturn(OrderFactory.getDefaultOrderDto());
    when(ticketService.getTicketById(anyInt())).thenReturn(TicketFactory.getDefaultTicket());
    when(userService.getUserById(anyInt())).thenReturn(UserFactory.getDefaultUser());
    when(orderRepository.save(any())).thenReturn(OrderFactory.getDefaultOrder());

    Item newItem = ItemFactory.getDefaultItem();
    newItem.setId(999);
    when(itemService.getItemById(newItem.getId())).thenReturn(newItem);

    OrderRequest request = OrderFactory.getDefaultOrderRequest();
    request.setItemsIds(Collections.singletonList(newItem.getId()));
    OrderDto result = orderService.updateOrder(request, ID);

    request.setItemsIds(new ArrayList<>());
    orderService.updateOrder(request, ID);


    assertEquals(expected, result);
  }

  @Test(expected = OrderNotFoundException.class)
  public void testUpdateOrder_OrderNotFoundException_fail() throws MailjetSocketTimeoutException, MailjetException {
    when(orderRepository.findById(anyInt())).thenReturn(Optional.empty());

    orderService.updateOrder(OrderFactory.getDefaultOrderRequest(), ID);
  }

  @Test
  public void testDeleteOrder_noExceptions_fail() {
    OrderDto expected = OrderFactory.getDefaultOrderDto();

    when(orderRepository.findById(anyInt())).thenReturn(Optional.of(new Order()));
    when(orderMapper.mapOrderToOrderDto(any())).thenReturn(expected);

    OrderDto result = orderService.deleteOrder(ID);

    assertEquals(expected, result);
  }

  @Test(expected = OrderNotFoundException.class)
  public void testDeleteOrder_OrderNotFoundException_fail() {
    when(orderRepository.findById(anyInt())).thenReturn(Optional.empty());

    orderService.deleteOrder(ID);
  }

  @Test
  public void testGetOrdersByDateBetween() {
    List<Order> expected = OrderFactory.getDefaultOrderList();

    when(orderRepository.findOrdersByDateOfPurchaseBetween(any(), any())).thenReturn(expected);

    List<Order> orders = orderService.getOrdersByDateBetween(START_DATE, END_DATE);

    assertEquals(expected, orders);
  }
}