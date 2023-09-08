package filmfocus.mappers;

import filmfocus.models.dtos.ProjectionDto;
import filmfocus.testUtils.factories.ProjectionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static filmfocus.testUtils.constants.ProjectionConstants.ID;
import static filmfocus.testUtils.constants.ProjectionConstants.PRICE;
import static filmfocus.testUtils.constants.ProjectionConstants.PROJECTION_HALL_DTO;
import static filmfocus.testUtils.constants.ProjectionConstants.PROJECTION_MOVIE_DTO;
import static filmfocus.testUtils.constants.ProjectionConstants.PROJECTION_PROGRAM_DTO;
import static filmfocus.testUtils.constants.ProjectionConstants.START_TIME;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProjectionMapperTest {

  @Mock
  private HallMapper hallMapper;

  @Mock
  private MovieMapper movieMapper;

  @Mock
  private ProgramMapper programMapper;

  @InjectMocks
  private ProjectionMapper projectionMapper;

  @Test
  public void testMapProjectionToProjectionDto_noExceptions_success() {
    when(hallMapper.mapHallToHallDto(any())).thenReturn(PROJECTION_HALL_DTO);
    when(movieMapper.mapMovieToMovieDto(any())).thenReturn(PROJECTION_MOVIE_DTO);
    when(programMapper.mapProgramToProgramDto(any())).thenReturn(PROJECTION_PROGRAM_DTO);

    ProjectionDto resultDto = projectionMapper.mapProjectionToProjectionDto(ProjectionFactory.getDefaultProjection());

    assertEquals(ID, resultDto.getId());
    assertEquals(PROJECTION_HALL_DTO.getId(), resultDto.getHall().getId());
    assertEquals(PROJECTION_HALL_DTO.getCapacity(), resultDto.getHall().getCapacity());
    assertEquals(PRICE, resultDto.getPrice(), 0.0);
    assertEquals(PROJECTION_PROGRAM_DTO, resultDto.getProgram());
    assertEquals(PROJECTION_MOVIE_DTO.getId(), resultDto.getMovie().getId());
    assertEquals(PROJECTION_MOVIE_DTO.getTitle(), resultDto.getMovie().getTitle());
    assertEquals(PROJECTION_MOVIE_DTO.getDescription(), resultDto.getMovie().getDescription());
    assertEquals(START_TIME, resultDto.getStartTime());
  }

  @Test
  public void testMapProjectionListToProjectionDtoList_noExceptions_success() {
    when(hallMapper.mapHallToHallDto(any())).thenReturn(PROJECTION_HALL_DTO);
    when(movieMapper.mapMovieToMovieDto(any())).thenReturn(PROJECTION_MOVIE_DTO);
    when(programMapper.mapProgramToProgramDto(any())).thenReturn(PROJECTION_PROGRAM_DTO);

    List<ProjectionDto> resultList =
      projectionMapper.mapProjectionListToProjectionDtoList(ProjectionFactory.getDefaultProjectionList());

    ProjectionDto resultDto = resultList.get(0);

    assertEquals(ID, resultDto.getId());
    assertEquals(PROJECTION_HALL_DTO.getId(), resultDto.getHall().getId());
    assertEquals(PROJECTION_HALL_DTO.getCapacity(), resultDto.getHall().getCapacity());
    assertEquals(PRICE, resultDto.getPrice(), 0.0);
    assertEquals(PROJECTION_PROGRAM_DTO, resultDto.getProgram());
    assertEquals(PROJECTION_MOVIE_DTO.getId(), resultDto.getMovie().getId());
    assertEquals(PROJECTION_MOVIE_DTO.getTitle(), resultDto.getMovie().getTitle());
    assertEquals(PROJECTION_MOVIE_DTO.getDescription(), resultDto.getMovie().getDescription());
    assertEquals(START_TIME, resultDto.getStartTime());
  }
}
