package filmfocus.services;

import filmfocus.exceptions.ItemAlreadyExistsException;
import filmfocus.exceptions.ItemNotFoundException;
import filmfocus.exceptions.NoAvailableItemsException;
import filmfocus.mappers.ItemMapper;
import filmfocus.models.dtos.ItemDto;
import filmfocus.models.entities.Item;
import filmfocus.models.requests.ItemRequest;
import filmfocus.repositories.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static filmfocus.utils.constants.ExceptionMessages.ITEM_ALREADY_EXISTS_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.ITEM_NOT_FOUND_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.NO_AVAILABLE_ITEMS_EXCEPTION;

@Service
public class ItemService {

  private static final Logger log = LoggerFactory.getLogger(ItemService.class);

  private final ItemRepository itemRepository;
  private final ItemMapper itemMapper;

  @Autowired
  public ItemService(ItemRepository itemRepository, ItemMapper itemMapper) {
    this.itemRepository = itemRepository;
    this.itemMapper = itemMapper;
  }

  public Item addItem(ItemRequest itemRequest) {
    log.info(String.format("An attempt to add item with name '%s' in thee database", itemRequest.getName()));

    this.itemRepository.findByName(itemRequest.getName()).ifPresent(item -> {
      log.error(String.format("Exception caught: %s", ITEM_ALREADY_EXISTS_MESSAGE));

      throw new ItemAlreadyExistsException(ITEM_ALREADY_EXISTS_MESSAGE);
    });

    log.info("An attempt to add a new item in the database");

    return itemRepository.save(new Item(itemRequest.getName(), itemRequest.getPrice(), itemRequest.getQuantity()));
  }

  public ItemDto getItemDtoById(int id) {
    log.info(String.format("An attempt to extract item DTO with id %d from the database", id));

    return itemMapper.mapItemToItemDto(getItemById(id));
  }

  public Item getItemById(int id) {
    log.info(String.format("An attempt to extract item with id %d from the database", id));

    return itemRepository.findById(id).orElseThrow(() -> {

      log.error(String.format("Exception caught: %s", ITEM_NOT_FOUND_MESSAGE));

      throw new ItemNotFoundException(ITEM_NOT_FOUND_MESSAGE);
    });
  }

  public ItemDto getItemDtoByName(String itemName) {
    log.info(String.format("An attempt to extract item with name %s from the database", itemName));

    return itemMapper.mapItemToItemDto(itemRepository.findByName(itemName).orElseThrow(() -> {
      log.error(String.format("Exception caught: %s", ITEM_NOT_FOUND_MESSAGE));

      throw new ItemNotFoundException(ITEM_NOT_FOUND_MESSAGE);
    }));
  }

  public List<ItemDto> getItemsByQuantity(int quantity, Boolean isBelow) {
    List<ItemDto> filteredItems = new ArrayList<>();
    List<ItemDto> itemList = getAllItems();

    for (ItemDto item : itemList) {

      if (isBelow) {

        if (item.getQuantity() < quantity) {
          filteredItems.add(item);
        }
      } else {

        if (item.getQuantity() > quantity) {

          filteredItems.add(item);
        }
      }
    }

    return filteredItems;
  }

  public List<ItemDto> getAllItems() {
    log.info("An attempt to extract all items from the database");

    return itemMapper.mapItemToItemDtoList(itemRepository.findAll());
  }

  public ItemDto editItem(ItemRequest itemRequest, int id) {
    ItemDto itemDto = getItemDtoById(id);

    log.info(String.format("An attempt to update item with id %d in the database", id));

    itemRepository.save(new Item(id, itemRequest.getName(), itemRequest.getPrice(), itemRequest.getQuantity()));

    return itemDto;
  }

  public ItemDto removeItem(int id) {
    ItemDto itemDto = getItemDtoById(id);

    itemRepository.deleteById(id);

    log.info(String.format("Item with id %d was deleted", id));

    return itemDto;
  }

  public int incrementItemQuantity(Item item) {
    int currentQuantity = item.getQuantity();
    int incrementedQuantity = currentQuantity + 1;

    item.setQuantity(incrementedQuantity);

    return incrementedQuantity;
  }

  public int decrementItemQuantity(Item item) {
    int currentQuantity = item.getQuantity();
    int decrementedQuantity = currentQuantity - 1;

    item.setQuantity(decrementedQuantity);

    if (currentQuantity <= 0) {
      log.error(String.format("Exception caught: %s", NO_AVAILABLE_ITEMS_EXCEPTION));

      throw new NoAvailableItemsException(NO_AVAILABLE_ITEMS_EXCEPTION);
    }

    return decrementedQuantity;
  }
}
