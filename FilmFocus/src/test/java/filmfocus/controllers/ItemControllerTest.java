package filmfocus.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import filmfocus.services.ItemService;
import filmfocus.testUtils.factories.ItemFactory;
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

import static filmfocus.testUtils.constants.ItemConstants.ID;
import static filmfocus.testUtils.constants.ItemConstants.IS_BELLOW;
import static filmfocus.testUtils.constants.ItemConstants.NAME;
import static filmfocus.testUtils.constants.ItemConstants.PRICE;
import static filmfocus.testUtils.constants.ItemConstants.QUANTITY;
import static filmfocus.utils.constants.URIConstants.ITEMS_ID_PATH;
import static filmfocus.utils.constants.URIConstants.ITEMS_PATH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@AutoConfigureMockMvc
public class ItemControllerTest {

  private MockMvc mockMvc;

  @Mock
  private ItemService itemService;
  @InjectMocks
  private ItemController itemController;

  @Before
  public void setup() {
    mockMvc = MockMvcBuilders
      .standaloneSetup(itemController)
      .build();
  }

  @Test
  public void testAddItem_noExceptions_success() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(ItemFactory.getDefaultItemRequest());

    when(itemService.addItem(any())).thenReturn(ItemFactory.getDefaultItem());

    mockMvc.perform(post(ITEMS_PATH)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isCreated())
           .andExpect(header().string("Location", ITEMS_PATH + "/" + ID));
  }

  @Test
  public void testGetAllItems_success() throws Exception {
    when(itemService.getAllItems()).thenReturn(
      Collections.singletonList(ItemFactory.getDefaultItemDto()));

    mockMvc.perform(get(ITEMS_PATH))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$").exists())
           .andExpect(jsonPath("$[0].id").value(ID))
           .andExpect(jsonPath("$[0].name").value(NAME))
           .andExpect(jsonPath("$[0].price").value(PRICE))
           .andExpect(jsonPath("$[0].quantity").value(QUANTITY));
  }

  @Test
  public void testGetItemByName_itemFound_success() throws Exception {
    when(itemService.getItemDtoByName(anyString())).thenReturn(ItemFactory.getDefaultItemDto());

    mockMvc.perform(get(ITEMS_PATH)
                      .queryParam("itemName", NAME))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$").exists())
           .andExpect(jsonPath("$.id").value(ID))
           .andExpect(jsonPath("$.name").value(NAME));
  }

  @Test
  public void testEditItem_noResponse_sucess() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(ItemFactory.getDefaultItemRequest());

    when(itemService.editItem(any(), eq(ID))).thenReturn(ItemFactory.getDefaultItemDto());

    mockMvc.perform(put(ITEMS_ID_PATH, ID)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isNoContent());
  }

  @Test
  public void testEditItem_requestedResponse_success() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(ItemFactory.getDefaultItemRequest());

    when(itemService.editItem(any(), eq(ID))).thenReturn(ItemFactory.getDefaultItemDto());

    mockMvc.perform(put(ITEMS_ID_PATH, ID)
                      .queryParam("returnOld", "true")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(ID));
  }

  @Test
  public void testDeleteItem_noResponse_success() throws Exception {
    when(itemService.removeItem(eq(ID))).thenReturn(ItemFactory.getDefaultItemDto());

    mockMvc.perform(delete(ITEMS_ID_PATH, ID))
           .andExpect(status().isNoContent());
  }

  @Test
  public void testDeleteItem_requestedResponse_success() throws Exception {
    when(itemService.removeItem(eq(ID))).thenReturn(ItemFactory.getDefaultItemDto());

    mockMvc.perform(delete(ITEMS_ID_PATH, ID)
                      .queryParam("returnOld", "true"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(ID));
  }

  @Test
  public void testGetItemsByQuantity() throws Exception {
    when(itemService.getItemsByQuantity(anyInt(), anyBoolean())).thenReturn(ItemFactory.getDefaultItemDtoList());


    mockMvc.perform(get(ITEMS_PATH)
                      .queryParam("quantity", String.valueOf(QUANTITY))
                      .queryParam("isBelow", String.valueOf(IS_BELLOW)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(ID))
           .andExpect(jsonPath("$[0].name").value(NAME))
           .andExpect(jsonPath("$[0].price").value(PRICE))
           .andExpect(jsonPath("$[0].quantity").value(QUANTITY));
  }
}
