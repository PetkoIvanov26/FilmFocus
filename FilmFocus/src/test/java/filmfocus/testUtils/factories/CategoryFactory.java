package filmfocus.testUtils.factories;

import filmfocus.models.dtos.CategoryDto;
import filmfocus.models.entities.Category;
import filmfocus.models.requests.CategoryRequest;

import java.util.Collections;
import java.util.List;

import static filmfocus.testUtils.constants.CategoryConstants.ID;
import static filmfocus.testUtils.constants.CategoryConstants.NAME;
import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class CategoryFactory {

  private CategoryFactory() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }

  public static CategoryRequest getDefaultCategoryRequest() {
    return new CategoryRequest(NAME);
  }

  public static Category getDefaultCategory() {
    return new Category(ID, NAME);
  }

  public static CategoryDto getDefaultCategoryDto() {
    return new CategoryDto(ID, NAME);
  }

  public static List<CategoryDto> getDefaultCategoryDtoList() {
    return Collections.singletonList(getDefaultCategoryDto());
  }

  public static List<Category> getDefaultCategoryList() {
    return Collections.singletonList(getDefaultCategory());
  }
}
