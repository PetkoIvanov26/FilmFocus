package filmfocus.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import filmfocus.services.ProjectionService;
import filmfocus.testUtils.constants.MovieConstants;
import filmfocus.testUtils.constants.ProgramConstants;
import filmfocus.testUtils.factories.CategoryFactory;
import filmfocus.testUtils.factories.CinemaFactory;
import filmfocus.testUtils.factories.HallFactory;
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

import java.time.Duration;

import static filmfocus.testUtils.constants.ProjectionConstants.ID;
import static filmfocus.testUtils.constants.ProjectionConstants.PRICE;
import static filmfocus.testUtils.constants.ProjectionConstants.START_TIME;
import static filmfocus.testUtils.factories.ProjectionFactory.getDefaultProjection;
import static filmfocus.testUtils.factories.ProjectionFactory.getDefaultProjectionDto;
import static filmfocus.testUtils.factories.ProjectionFactory.getDefaultProjectionDtoList;
import static filmfocus.testUtils.factories.ProjectionFactory.getDefaultProjectionRequest;
import static filmfocus.utils.constants.URIConstants.MOVIES_ID_PROJECTIONS_PATH;
import static filmfocus.utils.constants.URIConstants.PROGRAMS_ID_PROJECTIONS_PATH;
import static filmfocus.utils.constants.URIConstants.PROJECTIONS_ID_PATH;
import static filmfocus.utils.constants.URIConstants.PROJECTIONS_PATH;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
public class ProjectionControllerTest {

  private static final String START_TIME_STRING = "startTime";
  private static final String RETURN_OLD = "returnOld";
  private static final String IS_BEFORE = "isBefore";

  private final static ObjectMapper objectMapper = new ObjectMapper();
  private long expectedRuntimeSeconds;
  private MockMvc mockMvc;

  @Mock
  private ProjectionService projectionService;

  @InjectMocks
  private ProjectionController projectionController;

  @Before
  public void setup() {

    Duration expectedRuntime = Duration.parse("PT24H");
    expectedRuntimeSeconds = expectedRuntime.getSeconds();

    mockMvc = MockMvcBuilders
      .standaloneSetup(projectionController)
      .build();
  }

  @Test
  public void testAddProjection_projectionAdded_success() throws Exception {
    when(projectionService.addProjection(any())).thenReturn(getDefaultProjection());
    objectMapper.registerModule(new JavaTimeModule());
    String json = objectMapper.writeValueAsString(getDefaultProjectionRequest());

    mockMvc.perform(post(PROJECTIONS_PATH)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json))
           .andExpect(status().isCreated())
           .andExpect(header().string("Location", PROJECTIONS_PATH + "/" + ID));
  }

  @Test
  public void testGetProjectionsByProgramId_noExceptions_success() throws Exception {
    when(projectionService.getProjectionsByProgramId(anyInt())).thenReturn(getDefaultProjectionDtoList());

    mockMvc.perform(get(PROGRAMS_ID_PROJECTIONS_PATH, ID))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(ID))
           .andExpect(jsonPath("$[0].hall").value(HallFactory.getDefaultHallDto()))
           .andExpect(jsonPath("$[0].price").value(PRICE))
           .andExpect(jsonPath("$[0].program.id").value(ProgramConstants.ID))
           .andExpect(jsonPath("$[0].program.programDate[0]", is(ProgramConstants.DATE.getYear())))
           .andExpect(jsonPath("$[0].program.programDate[1]", is(ProgramConstants.DATE.getMonthValue())))
           .andExpect(jsonPath("$[0].program.programDate[2]", is(ProgramConstants.DATE.getDayOfMonth())))
           .andExpect(jsonPath("$[0].program.cinema").value(CinemaFactory.getDefaultCinemaDto()))
           .andExpect(jsonPath("$[0].movie.id").value(MovieConstants.ID))
           .andExpect(jsonPath("$[0].movie.title").value(MovieConstants.TITLE))
           .andExpect(jsonPath("$[0].movie.averageRating").value(MovieConstants.RATING))
           .andExpect(jsonPath("$[0].movie.description").value(MovieConstants.DESCRIPTION))
           .andExpect(jsonPath("$[0].movie.releaseDate[0]", is(MovieConstants.RELEASE_DATE.getYear())))
           .andExpect(jsonPath("$[0].movie.releaseDate[1]", is(MovieConstants.RELEASE_DATE.getDayOfMonth())))
           .andExpect(jsonPath("$[0].movie.releaseDate[2]", is(MovieConstants.RELEASE_DATE.getDayOfMonth())))
           .andExpect(jsonPath("$[0].movie.runtime").value(expectedRuntimeSeconds))
           .andExpect(jsonPath("$[0].movie.category").value(CategoryFactory.getDefaultCategoryDto()))
           .andExpect(jsonPath("$[0].startTime[0]", is(START_TIME.getHour())))
           .andExpect(jsonPath("$[0].startTime[1]", is(START_TIME.getMinute())))
           .andExpect(jsonPath("$[0].startTime[2]", is(START_TIME.getSecond())));
  }

  @Test
  public void testGetProjectionsByMovieId_noExceptions_success() throws Exception {
    when(projectionService.getProjectionsByMovieId(anyInt())).thenReturn(getDefaultProjectionDtoList());

    mockMvc.perform(get(MOVIES_ID_PROJECTIONS_PATH, ID))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(ID))
           .andExpect(jsonPath("$[0].hall").value(HallFactory.getDefaultHallDto()))
           .andExpect(jsonPath("$[0].price").value(PRICE))
           .andExpect(jsonPath("$[0].program.id").value(ProgramConstants.ID))
           .andExpect(jsonPath("$[0].program.programDate[0]", is(ProgramConstants.DATE.getYear())))
           .andExpect(jsonPath("$[0].program.programDate[1]", is(ProgramConstants.DATE.getMonthValue())))
           .andExpect(jsonPath("$[0].program.programDate[2]", is(ProgramConstants.DATE.getDayOfMonth())))
           .andExpect(jsonPath("$[0].program.cinema").value(CinemaFactory.getDefaultCinemaDto()))
           .andExpect(jsonPath("$[0].movie.id").value(MovieConstants.ID))
           .andExpect(jsonPath("$[0].movie.title").value(MovieConstants.TITLE))
           .andExpect(jsonPath("$[0].movie.averageRating").value(MovieConstants.RATING))
           .andExpect(jsonPath("$[0].movie.description").value(MovieConstants.DESCRIPTION))
           .andExpect(jsonPath("$[0].movie.releaseDate[0]", is(MovieConstants.RELEASE_DATE.getYear())))
           .andExpect(jsonPath("$[0].movie.releaseDate[1]", is(MovieConstants.RELEASE_DATE.getDayOfMonth())))
           .andExpect(jsonPath("$[0].movie.releaseDate[2]", is(MovieConstants.RELEASE_DATE.getDayOfMonth())))
           .andExpect(jsonPath("$[0].movie.runtime").value(expectedRuntimeSeconds))
           .andExpect(jsonPath("$[0].movie.category").value(CategoryFactory.getDefaultCategoryDto()))
           .andExpect(jsonPath("$[0].startTime[0]", is(START_TIME.getHour())))
           .andExpect(jsonPath("$[0].startTime[1]", is(START_TIME.getMinute())))
           .andExpect(jsonPath("$[0].startTime[2]", is(START_TIME.getSecond())));
  }

  @Test
  public void testGetProjectionsByStartTime_returnsProjectionsList_success() throws Exception {
    when(projectionService.getProjectionsByStartTime(any(), anyBoolean())).thenReturn(getDefaultProjectionDtoList());

    mockMvc.perform(get(PROJECTIONS_PATH)
                      .queryParam(START_TIME_STRING, String.valueOf(START_TIME))
                      .queryParam(IS_BEFORE, String.valueOf(true)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$[0].id").value(ID))
           .andExpect(jsonPath("$[0].hall").value(HallFactory.getDefaultHallDto()))
           .andExpect(jsonPath("$[0].price").value(PRICE))
           .andExpect(jsonPath("$[0].program.id").value(ProgramConstants.ID))
           .andExpect(jsonPath("$[0].program.programDate[0]", is(ProgramConstants.DATE.getYear())))
           .andExpect(jsonPath("$[0].program.programDate[1]", is(ProgramConstants.DATE.getMonthValue())))
           .andExpect(jsonPath("$[0].program.programDate[2]", is(ProgramConstants.DATE.getDayOfMonth())))
           .andExpect(jsonPath("$[0].program.cinema").value(CinemaFactory.getDefaultCinemaDto()))
           .andExpect(jsonPath("$[0].movie.id").value(MovieConstants.ID))
           .andExpect(jsonPath("$[0].movie.title").value(MovieConstants.TITLE))
           .andExpect(jsonPath("$[0].movie.averageRating").value(MovieConstants.RATING))
           .andExpect(jsonPath("$[0].movie.description").value(MovieConstants.DESCRIPTION))
           .andExpect(jsonPath("$[0].movie.releaseDate[0]", is(MovieConstants.RELEASE_DATE.getYear())))
           .andExpect(jsonPath("$[0].movie.releaseDate[1]", is(MovieConstants.RELEASE_DATE.getDayOfMonth())))
           .andExpect(jsonPath("$[0].movie.releaseDate[2]", is(MovieConstants.RELEASE_DATE.getDayOfMonth())))
           .andExpect(jsonPath("$[0].movie.runtime").value(expectedRuntimeSeconds))
           .andExpect(jsonPath("$[0].movie.category").value(CategoryFactory.getDefaultCategoryDto()))
           .andExpect(jsonPath("$[0].startTime[0]", is(START_TIME.getHour())))
           .andExpect(jsonPath("$[0].startTime[1]", is(START_TIME.getMinute())))
           .andExpect(jsonPath("$[0].startTime[2]", is(START_TIME.getSecond())));
  }

  @Test
  public void testUpdateProjection_returnOldTrue_success() throws Exception {
    when(projectionService.updateProjection(any(), anyInt())).thenReturn(getDefaultProjectionDto());
    objectMapper.registerModule(new JavaTimeModule());
    String json = objectMapper.writeValueAsString(getDefaultProjectionRequest());

    mockMvc.perform(put(PROJECTIONS_ID_PATH, ID)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json)
                      .queryParam(RETURN_OLD, String.valueOf(true)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(ID))
           .andExpect(jsonPath("$.hall").value(HallFactory.getDefaultHallDto()))
           .andExpect(jsonPath("$.price").value(PRICE))
           .andExpect(jsonPath("$.program.id").value(ProgramConstants.ID))
           .andExpect(jsonPath("$.program.programDate[0]", is(ProgramConstants.DATE.getYear())))
           .andExpect(jsonPath("$.program.programDate[1]", is(ProgramConstants.DATE.getMonthValue())))
           .andExpect(jsonPath("$.program.programDate[2]", is(ProgramConstants.DATE.getDayOfMonth())))
           .andExpect(jsonPath("$.program.cinema").value(CinemaFactory.getDefaultCinemaDto()))
           .andExpect(jsonPath("$.movie.id").value(MovieConstants.ID))
           .andExpect(jsonPath("$.movie.title").value(MovieConstants.TITLE))
           .andExpect(jsonPath("$.movie.averageRating").value(MovieConstants.RATING))
           .andExpect(jsonPath("$.movie.description").value(MovieConstants.DESCRIPTION))
           .andExpect(jsonPath("$.movie.releaseDate[0]", is(MovieConstants.RELEASE_DATE.getYear())))
           .andExpect(jsonPath("$.movie.releaseDate[1]", is(MovieConstants.RELEASE_DATE.getDayOfMonth())))
           .andExpect(jsonPath("$.movie.releaseDate[2]", is(MovieConstants.RELEASE_DATE.getDayOfMonth())))
           .andExpect(jsonPath("$.movie.runtime").value(expectedRuntimeSeconds))
           .andExpect(jsonPath("$.movie.category").value(CategoryFactory.getDefaultCategoryDto()))
           .andExpect(jsonPath("$.startTime[0]", is(START_TIME.getHour())))
           .andExpect(jsonPath("$.startTime[1]", is(START_TIME.getMinute())))
           .andExpect(jsonPath("$.startTime[2]", is(START_TIME.getSecond())));
  }

  @Test
  public void testUpdateProjection_returnOldFalse_success() throws Exception {
    when(projectionService.updateProjection(any(), anyInt())).thenReturn(getDefaultProjectionDto());
    objectMapper.registerModule(new JavaTimeModule());
    String json = objectMapper.writeValueAsString(getDefaultProjectionRequest());

    mockMvc.perform(put(PROJECTIONS_ID_PATH, ID)
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(json)
                      .queryParam(RETURN_OLD, String.valueOf(false)))
           .andExpect(status().isNoContent());
  }

  @Test
  public void testDeleteProjection_returnOldTrue_success() throws Exception {
    when(projectionService.deleteProjection(anyInt())).thenReturn(getDefaultProjectionDto());

    mockMvc.perform(delete(PROJECTIONS_ID_PATH, ID)
                      .queryParam(RETURN_OLD, String.valueOf(true)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(ID))
           .andExpect(jsonPath("$.hall").value(HallFactory.getDefaultHallDto()))
           .andExpect(jsonPath("$.price").value(PRICE))
           .andExpect(jsonPath("$.program.id").value(ProgramConstants.ID))
           .andExpect(jsonPath("$.program.programDate[0]", is(ProgramConstants.DATE.getYear())))
           .andExpect(jsonPath("$.program.programDate[1]", is(ProgramConstants.DATE.getMonthValue())))
           .andExpect(jsonPath("$.program.programDate[2]", is(ProgramConstants.DATE.getDayOfMonth())))
           .andExpect(jsonPath("$.program.cinema").value(CinemaFactory.getDefaultCinemaDto()))
           .andExpect(jsonPath("$.movie.id").value(MovieConstants.ID))
           .andExpect(jsonPath("$.movie.title").value(MovieConstants.TITLE))
           .andExpect(jsonPath("$.movie.averageRating").value(MovieConstants.RATING))
           .andExpect(jsonPath("$.movie.description").value(MovieConstants.DESCRIPTION))
           .andExpect(jsonPath("$.movie.releaseDate[0]", is(MovieConstants.RELEASE_DATE.getYear())))
           .andExpect(jsonPath("$.movie.releaseDate[1]", is(MovieConstants.RELEASE_DATE.getDayOfMonth())))
           .andExpect(jsonPath("$.movie.releaseDate[2]", is(MovieConstants.RELEASE_DATE.getDayOfMonth())))
           .andExpect(jsonPath("$.movie.runtime").value(expectedRuntimeSeconds))
           .andExpect(jsonPath("$.movie.category").value(CategoryFactory.getDefaultCategoryDto()))
           .andExpect(jsonPath("$.startTime[0]", is(START_TIME.getHour())))
           .andExpect(jsonPath("$.startTime[1]", is(START_TIME.getMinute())))
           .andExpect(jsonPath("$.startTime[2]", is(START_TIME.getSecond())));
  }

  @Test
  public void testDeleteProjection_returnOldFalse_success() throws Exception {
    when(projectionService.deleteProjection(anyInt())).thenReturn(getDefaultProjectionDto());

    mockMvc.perform(delete(PROJECTIONS_ID_PATH, ID))
           .andExpect(status().isNoContent());
  }
}
