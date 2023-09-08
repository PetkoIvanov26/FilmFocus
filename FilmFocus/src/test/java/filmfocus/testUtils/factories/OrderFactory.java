package filmfocus.testUtils.factories;

import filmfocus.models.dtos.OrderDto;
import filmfocus.models.entities.Order;
import filmfocus.models.requests.OrderRequest;

import java.util.Collections;
import java.util.List;

import static filmfocus.testUtils.constants.DiscountConstants.CODE;
import static filmfocus.testUtils.constants.OrderConstants.DATE_OF_PURCHASE;
import static filmfocus.testUtils.constants.OrderConstants.TOTAL_PRICE;
import static filmfocus.testUtils.constants.OrderConstants.ID;
import static filmfocus.testUtils.factories.ItemFactory.getDefaultItemDtoList;
import static filmfocus.testUtils.factories.ItemFactory.getDefaultItemList;
import static filmfocus.testUtils.factories.TicketFactory.getDefaultIdList;
import static filmfocus.testUtils.factories.UserFactory.getDefaultUser;
import static filmfocus.testUtils.factories.UserFactory.getDefaultUserDto;
import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class OrderFactory {

  private OrderFactory() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }

  public static OrderRequest getDefaultOrderRequest() {
    return new OrderRequest(ID, getDefaultIdList(), getDefaultIdList(), CODE);
  }

  public static Order getDefaultOrder() {

    return new Order(ID, DATE_OF_PURCHASE, getDefaultUser(), TicketFactory.getDefaultTicketList(),
                     getDefaultItemList(),
                     TOTAL_PRICE);
  }

  public static List<Order> getDefaultOrderList() {
    return Collections.singletonList(getDefaultOrder());
  }

  public static OrderDto getDefaultOrderDto() {
    return new OrderDto(ID, DATE_OF_PURCHASE, getDefaultUserDto(), TicketFactory.getDefaultTicketDtoList(),
                        getDefaultItemDtoList(),
                        TOTAL_PRICE);
  }

  public static List<OrderDto> getDefaultOrderDtoList() {
    return Collections.singletonList(getDefaultOrderDto());
  }
}
