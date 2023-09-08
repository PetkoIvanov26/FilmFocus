package filmfocus.mappers;

import filmfocus.models.dtos.CategoryDto;
import filmfocus.models.entities.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

  private static final Logger log = LoggerFactory.getLogger(CategoryMapper.class);

  public CategoryDto mapCategoryToCategoryDto(Category category) {
    log.info(String.format("The category %s is being mapped to a category DTO", category.getName()));
    return new CategoryDto(category.getId(), category.getName());
  }

  public List<CategoryDto> mapCategoryToCategoryDtoList(List<Category> categories) {
    return categories.stream()
                     .map(this::mapCategoryToCategoryDto)
                     .collect(Collectors.toList());
  }
}