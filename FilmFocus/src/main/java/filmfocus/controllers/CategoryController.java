package filmfocus.controllers;

import filmfocus.models.dtos.CategoryDto;
import filmfocus.models.entities.Category;
import filmfocus.models.requests.CategoryRequest;
import filmfocus.services.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static filmfocus.utils.constants.URIConstants.CATEGORIES_ID_PATH;
import static filmfocus.utils.constants.URIConstants.CATEGORIES_PATH;

@RestController
public class CategoryController {

  private static final Logger log = LoggerFactory.getLogger(CategoryController.class);
  private final CategoryService categoryService;

  @Autowired
  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @PostMapping(CATEGORIES_PATH)
  public ResponseEntity<Void> addCategory(@RequestBody @Valid CategoryRequest categoryRequest) {
    Category category = categoryService.addCategory(categoryRequest);
    log.info("A request for a category to be added has been submitted");

    URI location = UriComponentsBuilder
      .fromUriString(CATEGORIES_ID_PATH)
      .buildAndExpand(category.getId())
      .toUri();

    return ResponseEntity.created(location).build();
  }

  @GetMapping(CATEGORIES_PATH)
  public ResponseEntity<List<CategoryDto>> getAllCategories() {
    List<CategoryDto> categories = categoryService.getAllCategories();
    log.info("All categories were requested from the database");

    return ResponseEntity.ok(categories);
  }

  @GetMapping(value = CATEGORIES_PATH, params = "categoryName")
  public ResponseEntity<CategoryDto> getCategoryByName(@RequestParam String categoryName) {
    CategoryDto category = categoryService.getCategoryDtoByName(categoryName);
    log.info(String.format("Category with name %s has been requested from database", categoryName));

    return ResponseEntity.ok(category);
  }

  @PutMapping(CATEGORIES_ID_PATH)
  public ResponseEntity<CategoryDto> updateCategory(
    @RequestBody @Valid CategoryRequest categoryRequest,
    @PathVariable int id,
    @RequestParam(required = false) boolean returnOld) {

    CategoryDto categoryDto = categoryService.updateCategory(categoryRequest, id);
    log.info(String.format("Category with id %d was updated", id));

    return returnOld ? ResponseEntity.ok(categoryDto) : ResponseEntity.noContent().build();
  }

  @DeleteMapping(CATEGORIES_ID_PATH)
  public ResponseEntity<CategoryDto> deleteCategory(
    @PathVariable int id, @RequestParam(required = false) boolean returnOld) {

    CategoryDto categoryDto = categoryService.deleteCategory(id);
    log.info(String.format("Category with id %d was deleted", id));

    return returnOld ? ResponseEntity.ok(categoryDto) : ResponseEntity.noContent().build();
  }
}
