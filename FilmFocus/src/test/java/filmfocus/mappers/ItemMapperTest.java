package filmfocus.mappers;

import filmfocus.models.dtos.ItemDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static filmfocus.testUtils.constants.ItemConstants.ID;
import static filmfocus.testUtils.constants.ItemConstants.NAME;
import static filmfocus.testUtils.constants.ItemConstants.PRICE;
import static filmfocus.testUtils.constants.ItemConstants.QUANTITY;
import static filmfocus.testUtils.factories.ItemFactory.getDefaultItem;
import static filmfocus.testUtils.factories.ItemFactory.getDefaultItemList;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ItemMapperTest {

    @InjectMocks
    private ItemMapper itemMapper;

    @Test
    public void testMapItemToItemDto_success() {
        ItemDto itemDto = itemMapper.mapItemToItemDto(getDefaultItem());

        assertEquals(itemDto.getId(), ID);
        assertEquals(itemDto.getName(), NAME);
        assertEquals(itemDto.getPrice(), PRICE, 0.0);
        assertEquals(itemDto.getQuantity(), QUANTITY);
    }

    @Test
    public void testMapItemToItemDtoList_success() {
        List<ItemDto> itemDtos = itemMapper.mapItemToItemDtoList(getDefaultItemList());

        ItemDto itemDto = itemDtos.get(0);
        assertEquals(itemDto.getId(), ID);
        assertEquals(itemDto.getName(), NAME);
        assertEquals(itemDto.getPrice(), PRICE, 0.0);
        assertEquals(itemDto.getQuantity(), QUANTITY);
    }
}
