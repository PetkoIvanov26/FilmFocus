package filmfocus.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import filmfocus.services.DiscountService;
import filmfocus.testUtils.factories.DiscountFactory;
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

import static filmfocus.testUtils.constants.DiscountConstants.CODE;
import static filmfocus.testUtils.constants.DiscountConstants.ID;
import static filmfocus.testUtils.constants.DiscountConstants.PERCENTAGE;
import static filmfocus.testUtils.constants.DiscountConstants.TYPE;
import static filmfocus.utils.constants.URIConstants.DISCOUNTS_ID_PATH;
import static filmfocus.utils.constants.URIConstants.DISCOUNTS_PATH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
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
public class DiscountControllerTest {

  private static final String DISCOUNTS_PATH_WITH_ID = "/discounts/" + ID;

  private static final ObjectMapper mapper = new ObjectMapper();

  private MockMvc mockMvc;

  @Mock
  private DiscountService discountService;

  @InjectMocks
  private DiscountController discountController;

  @Before
  public void setup() {
    mockMvc = MockMvcBuilders
      .standaloneSetup(discountController)
      .build();
  }

  @Test
  public void testAddDiscount_noExceptions_success() throws Exception {
    String json = mapper.writeValueAsString(DiscountFactory.getDefaultDiscountRequest());

    when(discountService.addDiscount(any())).thenReturn(DiscountFactory.getDefaultDiscount());

    mockMvc.perform(post(DISCOUNTS_PATH)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isCreated())
           .andExpect(header().string("Location", DISCOUNTS_PATH + "/" + ID));
  }

  @Test
  public void testGetAllDiscounts_singleDiscount_success() throws Exception {
    when(discountService.getAllDiscountDtos()).thenReturn(DiscountFactory.getDefaultDiscountDtoList());

    mockMvc.perform(get(DISCOUNTS_PATH))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(ID))
           .andExpect(jsonPath("$[0].type").value(TYPE))
           .andExpect(jsonPath("$[0].code").value(CODE))
           .andExpect(jsonPath("$[0].percentage").value(PERCENTAGE));
  }

  @Test
  public void testGetDiscountByType_discountFound_success() throws Exception {
    when(discountService.getDiscountDtoByType(anyString())).thenReturn(DiscountFactory.getDefaultDiscountDto());

    mockMvc.perform(get(DISCOUNTS_PATH)
                      .queryParam("type", TYPE))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(ID))
           .andExpect(jsonPath("$.type").value(TYPE))
           .andExpect(jsonPath("$.code").value(CODE))
           .andExpect(jsonPath("$.percentage").value(PERCENTAGE));
  }

  @Test
  public void testUpdateDiscount_discountUpdated_returnsOk() throws Exception {
    String json = mapper.writeValueAsString(DiscountFactory.getDefaultDiscountRequest());
    when(discountService.updateDiscount(any(), anyInt())).thenReturn(DiscountFactory.getDefaultDiscountDto());

    mockMvc.perform(put(DISCOUNTS_ID_PATH, ID)
                      .queryParam("returnOld", String.valueOf(true))
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(ID))
           .andExpect(jsonPath("$.type").value(TYPE))
           .andExpect(jsonPath("$.code").value(CODE))
           .andExpect(jsonPath("$.percentage").value(PERCENTAGE));
  }

  @Test
  public void testUpdateDiscount_discountUpdated_returnsNoContent() throws Exception {
    String json = mapper.writeValueAsString(DiscountFactory.getDefaultDiscountRequest());
    when(discountService.updateDiscount(any(), anyInt())).thenReturn(DiscountFactory.getDefaultDiscountDto());

    mockMvc.perform(put(DISCOUNTS_ID_PATH, ID)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isNoContent());
  }

  @Test
  public void testDeleteDiscount_discountDeleted_returnsOk() throws Exception {
    when(discountService.deleteDiscount(anyInt())).thenReturn(DiscountFactory.getDefaultDiscountDto());

    mockMvc.perform(delete(DISCOUNTS_ID_PATH, ID)
                      .queryParam("returnOld", String.valueOf(true)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(ID))
           .andExpect(jsonPath("$.type").value(TYPE))
           .andExpect(jsonPath("$.code").value(CODE))
           .andExpect(jsonPath("$.percentage").value(PERCENTAGE));
  }

  @Test
  public void testDeleteDiscount_discountDeleted_returnsNoContent() throws Exception {
    when(discountService.deleteDiscount(anyInt())).thenReturn(DiscountFactory.getDefaultDiscountDto());

    mockMvc.perform(delete(DISCOUNTS_ID_PATH, ID))
           .andExpect(status().isNoContent());
  }
}
