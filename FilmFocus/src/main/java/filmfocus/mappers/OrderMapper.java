package filmfocus.mappers;

import filmfocus.models.dtos.OrderDto;
import filmfocus.models.entities.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

  private static final Logger log = LoggerFactory.getLogger(OrderMapper.class);

  private final UserMapper userMapper;
  private final TicketMapper ticketMapper;
  private final ItemMapper itemMapper;

  @Autowired
  public OrderMapper(UserMapper userMapper, TicketMapper ticketMapper, ItemMapper itemMapper) {
    this.userMapper = userMapper;
    this.ticketMapper = ticketMapper;
    this.itemMapper = itemMapper;
  }

  public OrderDto mapOrderToOrderDto(Order order) {
    log.info(String.format("The order with an id %d is being mapped to an order DTO", order.getId()));
    return new OrderDto(order.getId(), order.getDateOfPurchase(),
                        userMapper.mapUserToUserDto(order.getUser()),
                        ticketMapper.mapTicketToDtoList(order.getTickets()),
                        itemMapper.mapItemToItemDtoList(order.getItems()),
                        order.getTotalPrice());
  }

  public List<OrderDto> mapOrderToOrderDtoList(List<Order> orders) {
    return orders.stream()
                 .map(this::mapOrderToOrderDto)
                 .collect(Collectors.toList());
  }
}
