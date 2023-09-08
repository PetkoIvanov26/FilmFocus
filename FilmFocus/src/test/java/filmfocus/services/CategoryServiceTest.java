package filmfocus.services;

import filmfocus.exceptions.CategoryAlreadyExistsException;
import filmfocus.exceptions.CategoryNotFoundException;
import filmfocus.mappers.CategoryMapper;
import filmfocus.models.dtos.CategoryDto;
import filmfocus.models.entities.Category;
import filmfocus.repositories.CategoryRepository;
import filmfocus.testUtils.constants.CategoryConstants;
import filmfocus.testUtils.factories.CategoryFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static filmfocus.testUtils.constants.CategoryConstants.ID;
import static filmfocus.testUtils.constants.CategoryConstants.NAME;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceTest {

  @Mock
  private CategoryMapper categoryMapper;

  @Mock
  private CategoryRepository categoryRepository;

  @InjectMocks
  private CategoryService categoryService;

  @Test
  public void testAddCategory_noExceptions_success() {
    Category expected = CategoryFactory.getDefaultCategory();
    when(categoryRepository.save(any())).thenReturn(expected);

    Category category = categoryService.addCategory(CategoryFactory.getDefaultCategoryRequest());

    assertEquals(expected, category);
  }

  @Test
  public void testGetAllCategories_noExceptions_success() {
    List<CategoryDto> expected = CategoryFactory.getDefaultCategoryDtoList();

    when(categoryMapper.mapCategoryToCategoryDtoList(any())).thenReturn(expected);
    when(categoryRepository.findAll()).thenReturn(CategoryFactory.getDefaultCategoryList());

    List<CategoryDto> result = categoryService.getAllCategories();

    assertEquals(expected, result);
  }

  @Test
  public void testGetCategoryDtoById_noExceptions_success() {
    CategoryDto expected = CategoryFactory.getDefaultCategoryDto();
    when(categoryMapper.mapCategoryToCategoryDto(any())).thenReturn(expected);
    when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(CategoryFactory.getDefaultCategory()));

    CategoryDto categoryDto = categoryService.getCategoryDtoById(ID);

    assertEquals(expected, categoryDto);
  }

  @Test(expected = CategoryNotFoundException.class)
  public void testGetCategoryDtoById_throwsCategoryNotFoundException() {
    when(categoryRepository.findById(anyInt())).thenReturn(Optional.empty());

    categoryService.getCategoryDtoById(ID);
  }

  @Test
  public void testGetCategoryDtoByName_noExceptions_success() {
    CategoryDto expected = CategoryFactory.getDefaultCategoryDto();
    when(categoryMapper.mapCategoryToCategoryDto(any())).thenReturn(expected);
    when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(new Category()));

    CategoryDto categoryDto = categoryService.getCategoryDtoByName(NAME);

    assertEquals(expected, categoryDto);
  }

  @Test(expected = CategoryNotFoundException.class)
  public void testGetCategoryDtoByName_throwsCategoryNotFoundException() {
    when(categoryRepository.findByName(anyString())).thenReturn(Optional.empty());

    categoryService.getCategoryDtoByName(NAME);
  }

  @Test
  public void testUpdateCategory_success() {
    CategoryDto expected = CategoryFactory.getDefaultCategoryDto();
    when(categoryMapper.mapCategoryToCategoryDto(any())).thenReturn(expected);
    when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(CategoryFactory.getDefaultCategory()));
    when(categoryRepository.save(any())).thenReturn(CategoryFactory.getDefaultCategory());

    CategoryDto categoryDto = categoryService.updateCategory(CategoryFactory.getDefaultCategoryRequest(), ID);

    assertEquals(expected, categoryDto);
  }

  @Test(expected = CategoryAlreadyExistsException.class)
  public void testUpdateCategory_categoryExists_throws() {
    when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(new Category()));

    categoryService.addCategory(CategoryFactory.getDefaultCategoryRequest());
  }

  @Test
  public void testDeleteCategory_success() {
    CategoryDto expected = CategoryFactory.getDefaultCategoryDto();
    when(categoryMapper.mapCategoryToCategoryDto(any())).thenReturn(expected);
    when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(CategoryFactory.getDefaultCategory()));

    CategoryDto categoryDto = categoryService.deleteCategory(ID);

    assertEquals(expected, categoryDto);
  }

  @Test(expected = CategoryNotFoundException.class)
  public void testGetCategoryById_categoryNotFound_throwsCategoryNotFoundException() {
    categoryService.getCategoryById(CategoryConstants.ID);
  }
}