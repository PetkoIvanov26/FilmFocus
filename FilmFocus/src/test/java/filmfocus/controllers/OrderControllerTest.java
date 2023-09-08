package filmfocus.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import filmfocus.models.dtos.OrderDto;
import filmfocus.services.OrderService;
import filmfocus.testUtils.factories.OrderFactory;
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

import java.util.Collections;
import java.util.List;

import static filmfocus.testUtils.constants.DiscountConstants.CODE;
import static filmfocus.testUtils.constants.RoleConstants.NAME;
import static filmfocus.testUtils.constants.UserConstants.DAY;
import static filmfocus.testUtils.constants.UserConstants.EMAIL;
import static filmfocus.testUtils.constants.UserConstants.FIRST_NAME;
import static filmfocus.testUtils.constants.UserConstants.ID;
import static filmfocus.testUtils.constants.UserConstants.LAST_NAME;
import static filmfocus.testUtils.constants.UserConstants.MONTH;
import static filmfocus.testUtils.constants.UserConstants.USERNAME;
import static filmfocus.testUtils.constants.UserConstants.YEAR;
import static filmfocus.utils.constants.URIConstants.ORDERS_ID_PATH;
import static filmfocus.utils.constants.URIConstants.ORDERS_PATH;
import static filmfocus.utils.constants.URIConstants.USERS_ID_ORDERS_PATH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

  private static final String RETURN_OLD = "returnOld";
  private final static ObjectMapper objectMapper = new ObjectMapper();

  private MockMvc mockMvc;

  @Mock
  private OrderService orderService;

  @InjectMocks
  private OrderController orderController;

  @Before
  public void setup() {
    mockMvc = MockMvcBuilders
      .standaloneSetup(orderController)
      .build();
  }

  @Test
  public void testAddOrder_orderAdded_success() throws Exception {
    String json = objectMapper.writeValueAsString(OrderFactory.getDefaultOrderRequest());
    when(orderService.addOrder(any())).thenReturn(OrderFactory.getDefaultOrder());

    mockMvc.perform(post(ORDERS_PATH)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json)
                      .queryParam("discountCode", CODE))
           .andExpect(status().isCreated())
           .andExpect(header().string("Location", ORDERS_PATH + "/" + ID));
  }

  @Test
  public void testMakeReservationWithUserId_orderAdded_success() throws Exception {
    String json = objectMapper.writeValueAsString(Collections.singletonList(OrderFactory.getDefaultOrderRequest()));
    when(orderService.makeReservationWithUserId(any(), anyInt(), anyString())).thenReturn(
      OrderFactory.getDefaultOrder());

    mockMvc.perform(post(USERS_ID_ORDERS_PATH, ID)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isCreated())
           .andExpect(header().string("Location", ORDERS_PATH + "/" + ID));
  }

  @Test
  public void testGetOrdersByUserId_noExceptions_success() throws Exception {
    List<OrderDto> defaultOrderDtoList = OrderFactory.getDefaultOrderDtoList();
    OrderDto orderDto = defaultOrderDtoList.get(0);
    when(orderService.getOrdersByUserId(anyInt())).thenReturn(defaultOrderDtoList);

    mockMvc.perform(get(USERS_ID_ORDERS_PATH, ID))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(orderDto.getId()))
           .andExpect(jsonPath("$[0].user.username").value(USERNAME))
           .andExpect(jsonPath("$[0].user.email").value(EMAIL))
           .andExpect(jsonPath("$[0].user.firstName").value(FIRST_NAME))
           .andExpect(jsonPath("$[0].user.lastName").value(LAST_NAME))
           .andExpect(jsonPath("$[0].user.joinDate[0]").value(YEAR))
           .andExpect(jsonPath("$[0].user.joinDate[1]").value(MONTH))
           .andExpect(jsonPath("$[0].user.joinDate[2]").value(DAY))
           .andExpect(jsonPath("$[0].user.roles[0].name").value(NAME))
           .andExpect(jsonPath("$[0].tickets[0].id").value(orderDto.getTickets().get(0).getId()))
           .andExpect(jsonPath("$[0].items[0]").value(orderDto.getItems().get(0)))
           .andExpect(jsonPath("$[0].totalPrice").value(orderDto.getTotalPrice()));
  }

  @Test
  public void testUpdateOrder_returnOldTrue_success() throws Exception {
    OrderDto orderDto = OrderFactory.getDefaultOrderDto();
    when(orderService.updateOrder(any(), anyInt())).thenReturn(orderDto);
    String json = objectMapper.writeValueAsString(OrderFactory.getDefaultOrderRequest());

    mockMvc.perform(put(ORDERS_ID_PATH, ID)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json)
                      .queryParam(RETURN_OLD, String.valueOf(true)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(orderDto.getId()))
           .andExpect(jsonPath("$.user.username").value(USERNAME))
           .andExpect(jsonPath("$.user.email").value(EMAIL))
           .andExpect(jsonPath("$.user.firstName").value(FIRST_NAME))
           .andExpect(jsonPath("$.user.lastName").value(LAST_NAME))
           .andExpect(jsonPath("$.user.joinDate[0]").value(YEAR))
           .andExpect(jsonPath("$.user.joinDate[1]").value(MONTH))
           .andExpect(jsonPath("$.user.joinDate[2]").value(DAY))
           .andExpect(jsonPath("$.user.roles[0].name").value(NAME))
           .andExpect(jsonPath("$.tickets[0].id").value(orderDto.getTickets().get(0).getId()))
           .andExpect(jsonPath("$.items[0]").value(orderDto.getItems().get(0)))
           .andExpect(jsonPath("$.totalPrice").value(orderDto.getTotalPrice()));
  }

  @Test
  public void testUpdateOrder_returnOldFalse_success() throws Exception {
    when(orderService.updateOrder(any(), anyInt())).thenReturn(OrderFactory.getDefaultOrderDto());
    String json = objectMapper.writeValueAsString(OrderFactory.getDefaultOrderRequest());

    mockMvc.perform(put(ORDERS_ID_PATH, ID)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isNoContent());
  }

  @Test
  public void testDeleteOrder_returnOldTrue_success() throws Exception {
    OrderDto orderDto = OrderFactory.getDefaultOrderDto();
    when(orderService.deleteOrder(anyInt())).thenReturn(orderDto);

    mockMvc.perform(delete(ORDERS_ID_PATH, ID)
                      .queryParam(RETURN_OLD, String.valueOf(true)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(orderDto.getId()))
           .andExpect(jsonPath("$.user.username").value(USERNAME))
           .andExpect(jsonPath("$.user.email").value(EMAIL))
           .andExpect(jsonPath("$.user.firstName").value(FIRST_NAME))
           .andExpect(jsonPath("$.user.lastName").value(LAST_NAME))
           .andExpect(jsonPath("$.user.joinDate[0]").value(YEAR))
           .andExpect(jsonPath("$.user.joinDate[1]").value(MONTH))
           .andExpect(jsonPath("$.user.joinDate[2]").value(DAY))
           .andExpect(jsonPath("$.user.roles[0].name").value(NAME))
           .andExpect(jsonPath("$.tickets[0].id").value(orderDto.getTickets().get(0).getId()))
           .andExpect(jsonPath("$.items[0]").value(orderDto.getItems().get(0)))
           .andExpect(jsonPath("$.totalPrice").value(orderDto.getTotalPrice()));
  }

  @Test
  public void testDeleteOrder_returnOldFalse_success() throws Exception {
    when(orderService.deleteOrder(anyInt())).thenReturn(OrderFactory.getDefaultOrderDto());

    mockMvc.perform(delete(ORDERS_ID_PATH, ID))
           .andExpect(status().isNoContent());
  }
}