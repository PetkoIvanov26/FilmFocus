package filmfocus.mappers;

import filmfocus.models.dtos.ItemDto;
import filmfocus.models.entities.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemMapper {

  private static final Logger log = LoggerFactory.getLogger(ItemMapper.class);

  public ItemDto mapItemToItemDto(Item item) {
    log.info(String.format("The item %s is being mapped to an item DTO", item.getName()));
    return new ItemDto(item.getId(), item.getName(), item.getPrice(), item.getQuantity());
  }

  public List<ItemDto> mapItemToItemDtoList(List<Item> item) {
    return item.stream()
               .map(this::mapItemToItemDto)
               .collect(Collectors.toList());
  }
}
