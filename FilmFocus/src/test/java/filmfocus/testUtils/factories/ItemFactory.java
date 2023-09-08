package filmfocus.testUtils.factories;

import filmfocus.models.dtos.ItemDto;
import filmfocus.models.entities.Item;
import filmfocus.models.requests.ItemRequest;

import java.util.Collections;
import java.util.List;

import static filmfocus.testUtils.constants.ItemConstants.ID;
import static filmfocus.testUtils.constants.ItemConstants.NAME;
import static filmfocus.testUtils.constants.ItemConstants.PRICE;
import static filmfocus.testUtils.constants.ItemConstants.QUANTITY;
import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class ItemFactory {

  private ItemFactory() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }

  public static Item getDefaultItem() {
    return new Item(ID, NAME, PRICE, QUANTITY);
  }

  public static List<Item> getDefaultItemList() {
    return Collections.singletonList(getDefaultItem());
  }

  public static ItemDto getDefaultItemDto() {
    return new ItemDto(ID, NAME, PRICE, QUANTITY);
  }

  public static ItemRequest getDefaultItemRequest() {
    return new ItemRequest(NAME, PRICE, QUANTITY);
  }

  public static List<ItemDto> getDefaultItemDtoList() {
    return Collections.singletonList(getDefaultItemDto());
  }
}
