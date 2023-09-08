package filmfocus.mappers;

import filmfocus.models.dtos.CategoryDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static filmfocus.testUtils.constants.CategoryConstants.ID;
import static filmfocus.testUtils.constants.CategoryConstants.NAME;
import static filmfocus.testUtils.factories.CategoryFactory.getDefaultCategory;
import static filmfocus.testUtils.factories.CategoryFactory.getDefaultCategoryList;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CategoryMapperTest {

    @InjectMocks
    private CategoryMapper categoryMapper;

    @Test
    public void testMapCategoryToCategoryDtoList_success() {
        List<CategoryDto> categoryDtos = categoryMapper.mapCategoryToCategoryDtoList(getDefaultCategoryList());

        CategoryDto categoryDto = categoryDtos.get(0);
        assertEquals(categoryDto.getId(), ID);
        assertEquals(categoryDto.getName(), NAME);
    }

    @Test
    public void testMapCategoryToCategoryDto_success() {
        CategoryDto actualDto = categoryMapper.mapCategoryToCategoryDto(getDefaultCategory());

        assertEquals(actualDto.getId(), ID);
        assertEquals(actualDto.getName(), NAME);
    }
}