package filmfocus.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import filmfocus.models.dtos.MovieDto;
import filmfocus.services.MovieService;
import filmfocus.testUtils.factories.CategoryFactory;
import filmfocus.testUtils.factories.MovieFactory;
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
import java.util.List;

import static filmfocus.testUtils.constants.MovieConstants.DESCRIPTION;
import static filmfocus.testUtils.constants.MovieConstants.ID;
import static filmfocus.testUtils.constants.MovieConstants.RATING;
import static filmfocus.testUtils.constants.MovieConstants.RELEASE_DATE;
import static filmfocus.testUtils.constants.MovieConstants.RUNTIME;
import static filmfocus.testUtils.constants.MovieConstants.TITLE;
import static filmfocus.testUtils.factories.MovieFactory.getDefaultMovieDto;
import static filmfocus.testUtils.factories.MovieFactory.getDefaultMovieDtoList;
import static filmfocus.testUtils.factories.MovieFactory.getDefaultMovieRequest;
import static filmfocus.utils.constants.URIConstants.CATEGORIES_ID_MOVIES_PATH;
import static filmfocus.utils.constants.URIConstants.MOVIES_ID_PATH;
import static filmfocus.utils.constants.URIConstants.MOVIES_PATH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
public class MovieControllerTest {

  private static final String RETURN_OLD = "returnOld";
  private final static ObjectMapper objectMapper = new ObjectMapper();
  private MockMvc mockMvc;
  @Mock
  private MovieService movieService;

  @InjectMocks
  private MovieController movieController;

  @Before
  public void setup() {
    mockMvc = MockMvcBuilders
      .standaloneSetup(movieController)
      .build();
  }

  @Test
  public void testGetMoviesByTitle_success() throws Exception {
    when(movieService.getMoviesByTitle(anyString(), anyDouble())).thenReturn(getDefaultMovieDtoList());

    mockMvc.perform(get(MOVIES_PATH)
                      .queryParam("title", TITLE)
                      .queryParam("minimalRating", String.valueOf(RATING)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(ID))
           .andExpect(jsonPath("$[0].title").value(TITLE))
           .andExpect(jsonPath("$[0].averageRating").value(RATING))
           .andExpect(jsonPath("$[0].description").value(DESCRIPTION))
           .andExpect(jsonPath("$[0].releaseDate[0]").value(RELEASE_DATE.getYear()))
           .andExpect(jsonPath("$[0].releaseDate[1]").value(RELEASE_DATE.getMonthValue()))
           .andExpect(jsonPath("$[0].releaseDate[2]").value(RELEASE_DATE.getDayOfMonth()))
           .andExpect(jsonPath("$[0].runtime").value(RUNTIME.getSeconds()))
           .andExpect(jsonPath("$[0].category.id").value(CategoryFactory.getDefaultCategoryDto().getId()))
           .andExpect(jsonPath("$[0].category.name").value(CategoryFactory.getDefaultCategoryDto().getName()));
  }

  @Test
  public void testGetMoviesByCategory_success() throws Exception {
    List<MovieDto> movieDtoList = getDefaultMovieDtoList();
    MovieDto movieDto = movieDtoList.get(0);
    when(movieService.getMoviesByCategory(anyInt(), anyDouble())).thenReturn(movieDtoList);
    mockMvc.perform(get(CATEGORIES_ID_MOVIES_PATH, ID)
                      .param("minRating", String.valueOf(RATING)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(movieDto.getId()))
           .andExpect(jsonPath("$[0].title").value(TITLE))
           .andExpect(jsonPath("$[0].averageRating").value(RATING))
           .andExpect(jsonPath("$[0].description").value(DESCRIPTION))
           .andExpect(jsonPath("$[0].releaseDate[0]").value(RELEASE_DATE.getYear()))
           .andExpect(jsonPath("$[0].releaseDate[1]").value(RELEASE_DATE.getMonthValue()))
           .andExpect(jsonPath("$[0].releaseDate[2]").value(RELEASE_DATE.getDayOfMonth()))
           .andExpect(jsonPath("$[0].runtime").value(RUNTIME.getSeconds()))
           .andExpect(jsonPath("$[0].category.id").value(CategoryFactory.getDefaultCategoryDto().getId()))
           .andExpect(jsonPath("$[0].category.name").value(CategoryFactory.getDefaultCategoryDto().getName()));
  }

  @Test
  public void testGetMoviesByReleaseDateAfter_success() throws Exception {
    when(movieService.getMoviesByReleaseDate(any(), any(Double.class), anyBoolean())).thenReturn(
      Collections.singletonList(getDefaultMovieDto()));
    mockMvc.perform(get(MOVIES_PATH)
                      .param("release", String.valueOf(RELEASE_DATE))
                      .param("rating", String.valueOf(RATING))
                      .param("isAfter", "true"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(ID))
           .andExpect(jsonPath("$[0].title").value(TITLE))
           .andExpect(jsonPath("$[0].averageRating").value(RATING))
           .andExpect(jsonPath("$[0].description").value(DESCRIPTION))
           .andExpect(jsonPath("$[0].releaseDate[0]").value(RELEASE_DATE.getYear()))
           .andExpect(jsonPath("$[0].releaseDate[1]").value(RELEASE_DATE.getMonthValue()))
           .andExpect(jsonPath("$[0].releaseDate[2]").value(RELEASE_DATE.getDayOfMonth()))
           .andExpect(jsonPath("$[0].runtime").value(RUNTIME.getSeconds()))
           .andExpect(jsonPath("$[0].category.id").value(CategoryFactory.getDefaultCategoryDto().getId()))
           .andExpect(jsonPath("$[0].category.name").value(CategoryFactory.getDefaultCategoryDto().getName()));
  }

  @Test
  public void testGetMoviesByMinRating_success() throws Exception {
    when(movieService.getMoviesByMinRating(anyDouble())).thenReturn(getDefaultMovieDtoList());

    mockMvc.perform(get(MOVIES_PATH)
                      .param("minRating", String.valueOf(RATING)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(ID))
           .andExpect(jsonPath("$[0].title").value(TITLE))
           .andExpect(jsonPath("$[0].averageRating").value(RATING))
           .andExpect(jsonPath("$[0].description").value(DESCRIPTION))
           .andExpect(jsonPath("$[0].releaseDate[0]").value(RELEASE_DATE.getYear()))
           .andExpect(jsonPath("$[0].releaseDate[1]").value(RELEASE_DATE.getMonthValue()))
           .andExpect(jsonPath("$[0].releaseDate[2]").value(RELEASE_DATE.getDayOfMonth()))
           .andExpect(jsonPath("$[0].runtime").value(RUNTIME.getSeconds()))
           .andExpect(jsonPath("$[0].category.id").value(CategoryFactory.getDefaultCategoryDto().getId()))
           .andExpect(jsonPath("$[0].category.name").value(CategoryFactory.getDefaultCategoryDto().getName()));
  }

  @Test
  public void testAddMovie_success() throws Exception {
    when(movieService.addMovie(any())).thenReturn(MovieFactory.getDefaultMovie());
    objectMapper.registerModule(new JavaTimeModule());
    String json = objectMapper.writeValueAsString(getDefaultMovieRequest());

    mockMvc.perform(post(MOVIES_PATH)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isCreated())
           .andExpect(header().string("Location", MOVIES_PATH + "/" + ID));
  }

  @Test
  public void testUpdateMovie_returnOldTrue_success() throws Exception {
    when(movieService.updateMovie(any(), anyInt())).thenReturn(MovieFactory.getDefaultMovieDto());
    objectMapper.registerModule(new JavaTimeModule());
    String json = objectMapper.writeValueAsString(getDefaultMovieRequest());

    mockMvc.perform(put(MOVIES_ID_PATH, ID)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json)
                      .queryParam(RETURN_OLD, String.valueOf(true)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(ID))
           .andExpect(jsonPath("$.title").value(TITLE))
           .andExpect(jsonPath("$.averageRating").value(RATING))
           .andExpect(jsonPath("$.description").value(DESCRIPTION))
           .andExpect(jsonPath("$.releaseDate[0]").value(RELEASE_DATE.getYear()))
           .andExpect(jsonPath("$.releaseDate[1]").value(RELEASE_DATE.getMonthValue()))
           .andExpect(jsonPath("$.releaseDate[2]").value(RELEASE_DATE.getDayOfMonth()))
           .andExpect(jsonPath("$.runtime").value(RUNTIME.getSeconds()))
           .andExpect(jsonPath("$.category.id").value(CategoryFactory.getDefaultCategoryDto().getId()))
           .andExpect(jsonPath("$.category.name").value(CategoryFactory.getDefaultCategoryDto().getName()));
  }

  @Test
  public void testGetMoviesByImdbTest() throws Exception {
    String imdb = "top";
    String movies = "[{\"title\":\"title1\", \"year\":\"2001\"},{\"title\":\"title2\", \"year\":\"2002\"}]";

    when(movieService.getImdbMovies(imdb)).thenReturn(movies);

    mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();

    mockMvc.perform(get(MOVIES_PATH)
                      .param("imdb", imdb)
                      .contentType(MediaType.APPLICATION_JSON))
           .andExpect(status().isOk())
           .andExpect(content().json(movies));
  }

  @Test
  public void testUpdateMovie_returnOldFalse_success() throws Exception {
    when(movieService.updateMovie(any(), anyInt())).thenReturn(MovieFactory.getDefaultMovieDto());
    objectMapper.registerModule(new JavaTimeModule());
    String json = objectMapper.writeValueAsString(getDefaultMovieRequest());

    mockMvc.perform(put(MOVIES_ID_PATH, ID)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json)
                      .queryParam(RETURN_OLD, String.valueOf(false)))
           .andExpect(status().isNoContent());
  }

  @Test
  public void testDeleteMovie_returnOldTrue_success() throws Exception {
    when(movieService.deleteMovie(anyInt())).thenReturn(MovieFactory.getDefaultMovieDto());

    mockMvc.perform(delete(MOVIES_ID_PATH, ID)
                      .queryParam(RETURN_OLD, String.valueOf(true)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(ID))
           .andExpect(jsonPath("$.title").value(TITLE))
           .andExpect(jsonPath("$.averageRating").value(RATING))
           .andExpect(jsonPath("$.description").value(DESCRIPTION))
           .andExpect(jsonPath("$.releaseDate[0]").value(RELEASE_DATE.getYear()))
           .andExpect(jsonPath("$.releaseDate[1]").value(RELEASE_DATE.getMonthValue()))
           .andExpect(jsonPath("$.releaseDate[2]").value(RELEASE_DATE.getDayOfMonth()))
           .andExpect(jsonPath("$.runtime").value(RUNTIME.getSeconds()))
           .andExpect(jsonPath("$.category.id").value(CategoryFactory.getDefaultCategoryDto().getId()))
           .andExpect(jsonPath("$.category.name").value(CategoryFactory.getDefaultCategoryDto().getName()));
  }

  @Test
  public void testDeleteMovie_returnOldFalse_success() throws Exception {
    when(movieService.deleteMovie(anyInt())).thenReturn(MovieFactory.getDefaultMovieDto());

    mockMvc.perform(delete(MOVIES_ID_PATH, ID))
           .andExpect(status().isNoContent());
  }
}