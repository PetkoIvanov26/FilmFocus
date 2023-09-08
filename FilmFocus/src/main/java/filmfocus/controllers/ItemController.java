package filmfocus.controllers;

import filmfocus.models.dtos.ItemDto;
import filmfocus.models.entities.Item;
import filmfocus.models.requests.ItemRequest;
import filmfocus.services.ItemService;
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

import static filmfocus.utils.constants.URIConstants.ITEMS_ID_PATH;
import static filmfocus.utils.constants.URIConstants.ITEMS_PATH;

@RestController
public class ItemController {

  private static final Logger log = LoggerFactory.getLogger(ItemController.class);

  private final ItemService itemService;

  @Autowired
  public ItemController(ItemService itemService) {
    this.itemService = itemService;
  }

  @PostMapping(ITEMS_PATH)
  public ResponseEntity<Void> addItem(@RequestBody @Valid ItemRequest itemRequest) {
    log.info("Creation of a new item request has been submitted");
    Item item = itemService.addItem(itemRequest);
    log.info("Created new item");

    URI location = UriComponentsBuilder
      .fromUriString(ITEMS_ID_PATH)
      .buildAndExpand(item.getId())
      .toUri();

    return ResponseEntity.created(location).build();
  }

  @GetMapping(ITEMS_PATH)
  public ResponseEntity<List<ItemDto>> getAllItems() {
    List<ItemDto> items = itemService.getAllItems();
    log.info("All items were requested from the database");

    return ResponseEntity.ok(items);
  }

  @GetMapping(value = ITEMS_PATH, params = "itemName")
  public ResponseEntity<ItemDto> getItemByName(@RequestParam String itemName) {
    ItemDto itemdto = itemService.getItemDtoByName(itemName);
    log.info(String.format("Item with name %s has been requested from database", itemName));

    return ResponseEntity.ok(itemdto);
  }

  @GetMapping(value = ITEMS_PATH, params = "quantity")
  public ResponseEntity<List<ItemDto>> getItemsByQuantity(
    @RequestParam int quantity,
    @RequestParam Boolean isBelow) {

    List<ItemDto> filteredItems = itemService.getItemsByQuantity(quantity, isBelow);
    log.info("Filtered items were requested from the database");

    return ResponseEntity.ok(filteredItems);
  }

  @PutMapping(ITEMS_ID_PATH)
  public ResponseEntity<ItemDto> updateItem(
    @RequestBody @Valid ItemRequest itemRequest, @PathVariable int id,
    @RequestParam(required = false) boolean returnOld) {

    ItemDto itemDto = itemService.editItem(itemRequest, id);
    log.info(String.format("Item with id %d was updated", id));

    return returnOld ? ResponseEntity.ok(itemDto) : ResponseEntity.noContent().build();
  }

  @DeleteMapping(ITEMS_ID_PATH)
  public ResponseEntity<ItemDto> deleteItem(
    @PathVariable int id,
    @RequestParam(required = false) boolean returnOld) {

    ItemDto itemDto = itemService.removeItem(id);
    log.info(String.format("Item with id %d was deleted", id));

    return returnOld ? ResponseEntity.ok(itemDto) : ResponseEntity.noContent().build();
  }
}
