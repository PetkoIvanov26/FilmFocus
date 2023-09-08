package filmfocus.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import filmfocus.services.CategoryService;
import filmfocus.testUtils.factories.CategoryFactory;
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

import static filmfocus.testUtils.constants.CategoryConstants.ID;
import static filmfocus.testUtils.constants.CategoryConstants.NAME;
import static filmfocus.utils.constants.URIConstants.CATEGORIES_ID_PATH;
import static filmfocus.utils.constants.URIConstants.CATEGORIES_PATH;
import static org.mockito.ArgumentMatchers.any;
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
public class CategoryControllerTest {

  private MockMvc mockMvc;

  @Mock
  private CategoryService categoryService;

  @InjectMocks
  private CategoryController categoryController;

  @Before
  public void setup() {
    mockMvc = MockMvcBuilders
      .standaloneSetup(categoryController)
      .build();
  }

  @Test
  public void testAddCategory_noExceptions_success() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(CategoryFactory.getDefaultCategoryRequest());

    when(categoryService.addCategory(any())).thenReturn(CategoryFactory.getDefaultCategory());

    mockMvc.perform(post(CATEGORIES_PATH)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isCreated())
           .andExpect(header().string("Location", CATEGORIES_PATH + "/" + ID));
  }

  @Test
  public void testGetAllCategories_singleCategory_success() throws Exception {
    when(categoryService.getAllCategories()).thenReturn(
      Collections.singletonList(CategoryFactory.getDefaultCategoryDto()));

    mockMvc.perform(get(CATEGORIES_PATH))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(ID))
           .andExpect(jsonPath("$[0].name").value(NAME));
  }

  @Test
  public void testGetAllCategories_emptyList_success() throws Exception {
    when(categoryService.getAllCategories()).thenReturn(
      Collections.singletonList(CategoryFactory.getDefaultCategoryDto()));

    mockMvc.perform(get(CATEGORIES_PATH))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$").exists())
           .andExpect(jsonPath("$[0].id").value(ID))
           .andExpect(jsonPath("$[0].name").value(NAME));
  }

  @Test
  public void testGetCategoryByName_categoryFound_success() throws Exception {
    when(categoryService.getCategoryDtoByName(anyString())).thenReturn(CategoryFactory.getDefaultCategoryDto());

    mockMvc.perform(get(CATEGORIES_PATH)
                      .queryParam("categoryName", NAME))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$").exists())
           .andExpect(jsonPath("$.id").value(ID))
           .andExpect(jsonPath("$.name").value(NAME));
  }

  @Test
  public void testUpdateCategory_noResponse_success() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(CategoryFactory.getDefaultCategoryRequest());

    when(categoryService.updateCategory(any(), eq(ID))).thenReturn(CategoryFactory.getDefaultCategoryDto());

    mockMvc.perform(put(CATEGORIES_ID_PATH, ID)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isNoContent());
  }

  @Test
  public void testUpdateCategory_requestedResponse_success() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(CategoryFactory.getDefaultCategoryRequest());

    when(categoryService.updateCategory(any(), eq(ID))).thenReturn(CategoryFactory.getDefaultCategoryDto());

    mockMvc.perform(put(CATEGORIES_ID_PATH, ID)
                      .queryParam("returnOld", "true")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(ID))
           .andExpect(jsonPath("$.name").value(NAME));
  }

  @Test
  public void testDeleteCategory_noResponse_success() throws Exception {
    when(categoryService.deleteCategory(eq(ID))).thenReturn(CategoryFactory.getDefaultCategoryDto());

    mockMvc.perform(delete(CATEGORIES_ID_PATH, ID))
           .andExpect(status().isNoContent());
  }

  @Test
  public void testDeleteCategory_requestedResponse_success() throws Exception {
    when(categoryService.deleteCategory(eq(ID))).thenReturn(CategoryFactory.getDefaultCategoryDto());

    mockMvc.perform(delete(CATEGORIES_ID_PATH, ID)
                      .queryParam("returnOld", "true"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(ID))
           .andExpect(jsonPath("$.name").value(NAME));
  }
}
