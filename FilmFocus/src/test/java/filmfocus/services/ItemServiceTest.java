package filmfocus.services;

import filmfocus.exceptions.ItemAlreadyExistsException;
import filmfocus.exceptions.ItemNotFoundException;
import filmfocus.exceptions.NoAvailableItemsException;
import filmfocus.mappers.ItemMapper;
import filmfocus.models.dtos.ItemDto;
import filmfocus.models.entities.Item;
import filmfocus.repositories.ItemRepository;
import filmfocus.testUtils.factories.ItemFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static filmfocus.testUtils.constants.ItemConstants.ID;
import static filmfocus.testUtils.constants.ItemConstants.IS_BELLOW;
import static filmfocus.testUtils.constants.ItemConstants.NAME;
import static filmfocus.testUtils.constants.ItemConstants.QUANTITY;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemService itemService;

    @Test
    public void testGetAllItems_noExceptions_success() {
        List<ItemDto> expected = ItemFactory.getDefaultItemDtoList();
        when(itemMapper.mapItemToItemDtoList(any())).thenReturn(expected);
        when(itemRepository.findAll()).thenReturn(ItemFactory.getDefaultItemList());

        List<ItemDto> actual = itemService.getAllItems();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetItemDtoById_itemFound_success() {
        ItemDto expected = ItemFactory.getDefaultItemDto();
        when(itemMapper.mapItemToItemDto(any())).thenReturn(expected);
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(ItemFactory.getDefaultItem()));

        ItemDto item = itemService.getItemDtoById(ID);

        assertEquals(expected, item);
    }

    @Test(expected = ItemNotFoundException.class)
    public void testGetItemDtoById_itemNotFound_throwsItemNotFoundException() {
        when(itemRepository.findById(anyInt())).thenThrow(new ItemNotFoundException(anyString()));
        itemService.getItemDtoById(ID);
    }

    @Test
    public void testFindItemById_itemFound_success() {
        Item expected = ItemFactory.getDefaultItem();
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(expected));

        Item item = itemService.getItemById(ID);

        assertEquals(expected, item);
    }

    @Test(expected = ItemNotFoundException.class)
    public void testFindItemById_itemNotFound_throwsItemNotFoundException() {
        when(itemRepository.findById(anyInt())).thenReturn(Optional.empty());
        itemService.getItemById(ID);
    }

    @Test
    public void testGetItemDtoByName_noExceptions_success() {
        ItemDto expected = ItemFactory.getDefaultItemDto();
        when(itemMapper.mapItemToItemDto(any())).thenReturn(expected);
        when(itemRepository.findByName(anyString())).thenReturn(Optional.of(ItemFactory.getDefaultItem()));

        ItemDto itemDto = itemService.getItemDtoByName(NAME);

        assertEquals(expected, itemDto);
    }

    @Test(expected = ItemNotFoundException.class)
    public void testGetItemDtoByName_throwsCategoryNotFoundException() {
        when(itemRepository.findByName(anyString())).thenReturn(Optional.empty());

        itemService.getItemDtoByName(NAME);
    }

    @Test
    public void testAddItem_noExceptions_success() {
        Item expected = ItemFactory.getDefaultItem();
        when(itemRepository.save(any())).thenReturn(expected);

        Item item = itemService.addItem(ItemFactory.getDefaultItemRequest());

        assertEquals(expected, item);
    }

    @Test(expected = ItemAlreadyExistsException.class)
    public void testAddItem_itemAlreadyExists_throwsExistingItemException() {
        when(itemRepository.findByName(anyString())).thenReturn(Optional.of(new Item()));

        itemService.addItem(ItemFactory.getDefaultItemRequest());
    }

    @Test
    public void testUpdateItem_itemUpdated_success() {
        ItemDto expected = ItemFactory.getDefaultItemDto();
        when(itemMapper.mapItemToItemDto(any())).thenReturn(expected);
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(ItemFactory.getDefaultItem()));
        when(itemRepository.save(any())).thenReturn(ItemFactory.getDefaultItem());

        ItemDto itemDto = itemService.editItem(ItemFactory.getDefaultItemRequest(), ID);

        assertEquals(expected, itemDto);
    }

    @Test
    public void testDeleteItem_itemDeleted_success() {
        ItemDto expected = ItemFactory.getDefaultItemDto();
        when(itemMapper.mapItemToItemDto(any())).thenReturn(expected);
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(ItemFactory.getDefaultItem()));

        ItemDto itemDto = itemService.removeItem(ID);

        assertEquals(expected, itemDto);
    }

    @Test
    public void testIncrementItemQuantity_shouldIncrementedItemQuantity_success() {
        Item item = new Item();
        item.setQuantity(5);

        int incrementedQuantity = itemService.incrementItemQuantity(item);

        assertEquals(6, incrementedQuantity);
        assertEquals(6, item.getQuantity());
    }

    @Test
    public void testDecrementItemQuantity_shouldDecrementItemQuantity_success() {
        Item item = new Item();
        item.setQuantity(5);

        int decrementedQuantity = itemService.decrementItemQuantity(item);

        assertEquals(4, decrementedQuantity);
        assertEquals(4, item.getQuantity());
    }

    @Test(expected = NoAvailableItemsException.class)
    public void testDecrementItemQuantity_shouldThrowNoAvailableItemsException() {
        Item item = new Item();
        item.setQuantity(0);

        itemService.decrementItemQuantity(item);
    }

    @Test
    public void testGetItemsByQuantity_noExceptions_isBelowFalse_success() {
        List<ItemDto> expected = ItemFactory.getDefaultItemDtoList();
        when(itemRepository.findAll()).thenReturn(ItemFactory.getDefaultItemList());
        when(itemMapper.mapItemToItemDtoList(any())).thenReturn(expected);

        List<ItemDto> resultList = itemService.getItemsByQuantity(QUANTITY - 1, IS_BELLOW);

        assertEquals(expected, resultList);
    }

    @Test
    public void testGetItemsByQuantity_noExceptions_isBelowTrue_success() {
        List<ItemDto> expected = ItemFactory.getDefaultItemDtoList();
        when(itemRepository.findAll()).thenReturn(ItemFactory.getDefaultItemList());
        when(itemMapper.mapItemToItemDtoList(any())).thenReturn(expected);

        List<ItemDto> resultList = itemService.getItemsByQuantity(QUANTITY + 1, true);

        assertEquals(expected, resultList);
    }
}
