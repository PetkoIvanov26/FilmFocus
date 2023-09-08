package filmfocus.mappers;

import filmfocus.models.dtos.MovieDto;
import filmfocus.testUtils.factories.CategoryFactory;
import filmfocus.testUtils.factories.MovieFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Objects;

import static filmfocus.testUtils.constants.MovieConstants.DESCRIPTION;
import static filmfocus.testUtils.constants.MovieConstants.ID;
import static filmfocus.testUtils.constants.MovieConstants.RATING;
import static filmfocus.testUtils.constants.MovieConstants.RELEASE_DATE;
import static filmfocus.testUtils.constants.MovieConstants.RUNTIME;
import static filmfocus.testUtils.constants.MovieConstants.TITLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MovieMapperTest {

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private MovieMapper movieMapper;

    @Test
    public void testMapMovieToMovieDto_success() {
        when(categoryMapper.mapCategoryToCategoryDto(any())).thenReturn(CategoryFactory.getDefaultCategoryDto());

        MovieDto movieDto = movieMapper.mapMovieToMovieDto(MovieFactory.getDefaultMovie());

        assertEquals(ID, movieDto.getId());
        assertEquals(TITLE, movieDto.getTitle());
        assertEquals(RATING, movieDto.getAverageRating(), 0.0);
        assertEquals(DESCRIPTION, movieDto.getDescription());
        assertTrue(RELEASE_DATE.isEqual(movieDto.getReleaseDate()));
        assertEquals(RUNTIME, movieDto.getRuntime());
        assertTrue(Objects.nonNull(movieDto.getCategory()));
    }

    @Test
    public void testMapMovieListToMovieDtoList_success() {
        when(categoryMapper.mapCategoryToCategoryDto(any())).thenReturn(CategoryFactory.getDefaultCategoryDto());

        List<MovieDto> movieDtos = movieMapper.mapMovieListToMovieDtoList(MovieFactory.getDefaultMovieList());
        MovieDto movieDto = movieDtos.get(0);

        assertEquals(ID, movieDto.getId());
        assertEquals(TITLE, movieDto.getTitle());
        assertEquals(RATING, movieDto.getAverageRating(), 0.0);
        assertEquals(DESCRIPTION, movieDto.getDescription());
        assertTrue(RELEASE_DATE.isEqual(movieDto.getReleaseDate()));
        assertEquals(RUNTIME, movieDto.getRuntime());
        assertTrue(Objects.nonNull(movieDto.getCategory()));
    }
}