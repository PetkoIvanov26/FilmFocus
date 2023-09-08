package filmfocus.services;

import filmfocus.exceptions.CategoryAlreadyExistsException;
import filmfocus.exceptions.CategoryNotFoundException;
import filmfocus.mappers.CategoryMapper;
import filmfocus.models.dtos.CategoryDto;
import filmfocus.models.entities.Category;
import filmfocus.models.requests.CategoryRequest;
import filmfocus.repositories.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static filmfocus.utils.constants.ExceptionMessages.CATEGORY_ALREADY_EXISTS_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.CATEGORY_NOT_FOUND_MESSAGE;

@Service
public class CategoryService {

  private final static Logger log = LoggerFactory.getLogger(CategoryService.class);

  private final CategoryMapper categoryMapper;
  private final CategoryRepository categoryRepository;

  @Autowired
  public CategoryService(CategoryMapper categoryMapper, CategoryRepository categoryRepository) {
    this.categoryMapper = categoryMapper;
    this.categoryRepository = categoryRepository;
  }

  public Category addCategory(CategoryRequest categoryRequest) {
    log.info("An attempt to save a category in the database");

    categoryValidation(categoryRequest);

    return categoryRepository.save(new Category(categoryRequest.getName()));
  }

  public List<CategoryDto> getAllCategories() {
    log.info("An attempt to extract all categories from the database");

    return categoryMapper.mapCategoryToCategoryDtoList(categoryRepository.findAll());
  }

  public CategoryDto getCategoryDtoById(int id) {
    log.info(String.format("An attempt to extract category with id %d from the database", id));

    return categoryMapper.mapCategoryToCategoryDto(getCategoryById(id));
  }

  public CategoryDto getCategoryDtoByName(String name) {
    log.info(String.format("An attempt to extract category with name %s from the database", name));

    return categoryMapper.mapCategoryToCategoryDto(categoryRepository.findByName(name).orElseThrow(() -> {

      log.error(String.format("Exception caught: %s", CATEGORY_NOT_FOUND_MESSAGE));

      throw new CategoryNotFoundException(CATEGORY_NOT_FOUND_MESSAGE);
    }));
  }

  public Category getCategoryById(int id) {
    log.info(String.format("An attempt to extract category with id %d from the database", id));

    return categoryRepository.findById(id).orElseThrow(() -> {

      log.error(String.format("Exception caught: %s", CATEGORY_NOT_FOUND_MESSAGE));

      throw new CategoryNotFoundException(CATEGORY_NOT_FOUND_MESSAGE);
    });
  }

  public CategoryDto updateCategory(CategoryRequest categoryRequest, int categoryId) {
    CategoryDto categoryDto = getCategoryDtoById(categoryId);

    log.info(String.format("An attempt to update category with id %d in the database", categoryId));

    categoryRepository.save(new Category(categoryId, categoryRequest.getName()));
    return categoryDto;
  }

  public CategoryDto deleteCategory(int categoryId) {
    CategoryDto categoryDto = getCategoryDtoById(categoryId);
    categoryRepository.deleteById(categoryId);

    log.info(String.format("Category with id %d was deleted", categoryId));

    return categoryDto;
  }

  private void categoryValidation(CategoryRequest categoryRequest) {
    categoryRepository.findByName(categoryRequest.getName()).ifPresent(category -> {
      log.error(String.format("Exception caught: %s", CATEGORY_ALREADY_EXISTS_MESSAGE));

      throw new CategoryAlreadyExistsException(CATEGORY_ALREADY_EXISTS_MESSAGE);
    });
  }
}
