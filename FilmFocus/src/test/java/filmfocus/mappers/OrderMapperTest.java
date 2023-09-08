package filmfocus.mappers;

import filmfocus.models.dtos.ItemDto;
import filmfocus.models.dtos.OrderDto;
import filmfocus.models.dtos.TicketDto;
import filmfocus.models.dtos.UserDto;
import filmfocus.models.entities.Order;
import filmfocus.testUtils.factories.OrderFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static filmfocus.testUtils.constants.OrderConstants.DATE_OF_PURCHASE;
import static filmfocus.testUtils.constants.OrderConstants.ID;
import static filmfocus.testUtils.constants.OrderConstants.TOTAL_PRICE;
import static filmfocus.testUtils.factories.ItemFactory.getDefaultItemDtoList;
import static filmfocus.testUtils.factories.OrderFactory.getDefaultOrder;
import static filmfocus.testUtils.factories.TicketFactory.getDefaultTicketDtoList;
import static filmfocus.testUtils.factories.UserFactory.getDefaultUserDto;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderMapperTest {

    @Mock
    private ItemMapper itemMapper;
    @Mock
    private TicketMapper ticketMapper;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private OrderMapper orderMapper;

    @Test
    public void testMapOrderToOrderDto_success() {
        Order order = getDefaultOrder();
        UserDto userDto = getDefaultUserDto();
        List<TicketDto> ticketDtoList = getDefaultTicketDtoList();
        List<ItemDto> itemDtoList = getDefaultItemDtoList();

        when(userMapper.mapUserToUserDto(any())).thenReturn(userDto);
        when(ticketMapper.mapTicketToDtoList(any())).thenReturn(ticketDtoList);
        when(itemMapper.mapItemToItemDtoList(any())).thenReturn(itemDtoList);

        OrderDto orderDto = orderMapper.mapOrderToOrderDto(order);

        assertEquals(ID, orderDto.getId());
        assertEquals(userDto, orderDto.getUser());
        assertEquals(ticketDtoList, orderDto.getTickets());
        assertEquals(itemDtoList, orderDto.getItems());
        assertEquals(TOTAL_PRICE, orderDto.getTotalPrice(), 1.1);
        assertEquals(DATE_OF_PURCHASE, orderDto.getDateOfPurchase());
    }

    @Test
    public void testMapOrderToOrderDtoList_success() {
        UserDto userDto = getDefaultUserDto();
        List<TicketDto> ticketDtoList = getDefaultTicketDtoList();
        List<ItemDto> itemDtoList = getDefaultItemDtoList();

        when(userMapper.mapUserToUserDto(any())).thenReturn(userDto);
        when(ticketMapper.mapTicketToDtoList(any())).thenReturn(ticketDtoList);
        when(itemMapper.mapItemToItemDtoList(any())).thenReturn(itemDtoList);

        List<OrderDto> orderDtos = orderMapper.mapOrderToOrderDtoList(OrderFactory.getDefaultOrderList());
        OrderDto orderDto = orderDtos.get(0);

        assertEquals(ID, orderDto.getId());
        assertEquals(userDto, orderDto.getUser());
        assertEquals(ticketDtoList, orderDto.getTickets());
        assertEquals(itemDtoList, orderDto.getItems());
        assertEquals(TOTAL_PRICE, orderDto.getTotalPrice(), 1.1);
        assertEquals(DATE_OF_PURCHASE, orderDto.getDateOfPurchase());
    }
}