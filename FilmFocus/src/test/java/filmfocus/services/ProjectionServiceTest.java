package filmfocus.services;

import filmfocus.exceptions.HallNotAvailableException;
import filmfocus.exceptions.ProgramNotFoundException;
import filmfocus.mappers.ProjectionMapper;
import filmfocus.models.dtos.ProjectionDto;
import filmfocus.models.entities.Movie;
import filmfocus.models.entities.Program;
import filmfocus.models.entities.Projection;
import filmfocus.repositories.ProjectionRepository;
import filmfocus.testUtils.factories.HallFactory;
import filmfocus.testUtils.factories.MovieFactory;
import filmfocus.testUtils.factories.ProgramFactory;
import filmfocus.testUtils.factories.ProjectionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static filmfocus.testUtils.constants.ProjectionConstants.ID;
import static filmfocus.testUtils.constants.ProjectionConstants.START_TIME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProjectionServiceTest {

  @Mock
  private ProjectionRepository projectionRepository;

  @Mock
  private ProjectionMapper projectionMapper;

  @Mock
  private HallService hallService;

  @Mock
  private ProgramService programService;

  @Mock
  private MovieService movieService;

  @InjectMocks
  private ProjectionService projectionService;

  @Test
  public void testGetProjectionsByProgramId_programFound_success() {
    Program program = ProgramFactory.getDefaultProgram();
    List<ProjectionDto> expected = ProjectionFactory.getDefaultProjectionDtoList();

    when(programService.getProgramById(anyInt())).thenReturn(program);
    when(projectionMapper.mapProjectionListToProjectionDtoList(any())).thenReturn(expected);
    when(projectionRepository.findProjectionsByProgramId(anyInt())).thenReturn(
      ProjectionFactory.getDefaultProjectionList());

    List<ProjectionDto> resultList = projectionService.getProjectionsByProgramId(program.getId());

    assertEquals(expected, resultList);
  }

  @Test
  public void testGetProjectionByMovieId_movieFound_success() {
    Movie movie = MovieFactory.getDefaultMovie();
    List<ProjectionDto> expected = ProjectionFactory.getDefaultProjectionDtoList();

    when(movieService.getMovieById(anyInt())).thenReturn(movie);
    when(projectionMapper.mapProjectionListToProjectionDtoList(any())).thenReturn(expected);
    when(projectionRepository.findProjectionsByMovieId(anyInt())).thenReturn(
      ProjectionFactory.getDefaultProjectionList());

    List<ProjectionDto> resultList = projectionService.getProjectionsByMovieId(movie.getId());

    assertEquals(expected, resultList);
  }

  @Test
  public void testGetProjectionsByStartTime_isBeforeTrue_success() {
    List<ProjectionDto> expected = ProjectionFactory.getDefaultProjectionDtoList();

    when(projectionMapper.mapProjectionListToProjectionDtoList(any())).thenReturn(expected);
    when(projectionRepository.findProjectionsByStartTimeBefore(any())).thenReturn(
      ProjectionFactory.getDefaultProjectionList());

    List<ProjectionDto> resultList = projectionService.getProjectionsByStartTime(START_TIME, true);

    assertEquals(expected, resultList);
  }

  @Test
  public void testGetProjectionsByStartTime_isBeforeFalse_success() {
    List<ProjectionDto> expected = ProjectionFactory.getDefaultProjectionDtoList();

    when(projectionMapper.mapProjectionListToProjectionDtoList(any())).thenReturn(expected);
    when(projectionRepository.findProjectionsByStartTimeAfter(any())).thenReturn(
      ProjectionFactory.getDefaultProjectionList());

    List<ProjectionDto> resultList = projectionService.getProjectionsByStartTime(START_TIME, false);

    assertEquals(expected, resultList);
  }

  @Test
  public void testAddProjection_noExceptions_success() {
    Projection expected = ProjectionFactory.getDefaultProjection();

    when(projectionRepository.save(any())).thenReturn(expected);
    when(hallService.getHallById(anyInt())).thenReturn(HallFactory.getDefaultHall());
    when(programService.getProgramById(anyInt())).thenReturn(ProgramFactory.getDefaultProgram());
    when(movieService.getMovieById(anyInt())).thenReturn(MovieFactory.getDefaultMovie());

    Projection projection = projectionService.addProjection(ProjectionFactory.getDefaultProjectionRequest());

    assertEquals(expected, projection);
  }

  @Test(expected = HallNotAvailableException.class)
  public void testAddProjection_hallNotAvailable_throwsHallNotAvailableException() {
    when(hallService.getHallById(anyInt())).thenReturn(HallFactory.getDefaultHall());
    when(programService.getProgramById(anyInt())).thenReturn(ProgramFactory.getDefaultProgram());

    ProjectionService spyProjectionService = Mockito.spy(projectionService);
    doReturn(false).when(spyProjectionService).isHallAvailable(anyInt(), anyInt(), any(LocalTime.class));

    spyProjectionService.addProjection(ProjectionFactory.getDefaultProjectionRequest());
  }

  @Test
  public void testUpdateProjection_projectionUpdated_success() {
    ProjectionDto expected = ProjectionFactory.getDefaultProjectionDto();


    when(projectionMapper.mapProjectionToProjectionDto(any())).thenReturn(expected);
    when(hallService.getHallById(anyInt())).thenReturn(HallFactory.getDefaultHall());
    when(programService.getProgramById(anyInt())).thenReturn(ProgramFactory.getDefaultProgram());
    when(projectionRepository.findById(anyInt())).thenReturn(Optional.of(ProjectionFactory.getDefaultProjection()));
    when(projectionRepository.save(any())).thenReturn(ProjectionFactory.getDefaultProjection());


    ProjectionDto projectionDto =
      projectionService.updateProjection(ProjectionFactory.getDefaultProjectionRequest(), ID);

    assertEquals(expected, projectionDto);
  }

  @Test(expected = HallNotAvailableException.class)
  public void testUpdateProjection_hallNotAvailable_throwsHallNotAvailableException() {
    ProjectionDto expected = ProjectionFactory.getDefaultProjectionDto();
    when(projectionMapper.mapProjectionToProjectionDto(any(Projection.class))).thenReturn(expected);
    when(hallService.getHallById(anyInt())).thenReturn(HallFactory.getDefaultHall());
    when(programService.getProgramById(anyInt())).thenReturn(ProgramFactory.getDefaultProgram());
    when(projectionRepository.findById(anyInt())).thenReturn(Optional.of(ProjectionFactory.getDefaultProjection()));


    ProjectionService spyProjectionService = Mockito.spy(projectionService);
    doReturn(false).when(spyProjectionService).isHallAvailable(anyInt(), anyInt(), any(LocalTime.class));

    spyProjectionService.updateProjection(ProjectionFactory.getDefaultProjectionRequest(), ID);
  }

  @Test
  public void testDeleteProjection_projectionDeleted_success() {
    ProjectionDto expected = ProjectionFactory.getDefaultProjectionDto();

    when(projectionMapper.mapProjectionToProjectionDto(any())).thenReturn(expected);
    when(projectionRepository.findById(anyInt())).thenReturn(Optional.of(new Projection()));

    ProjectionDto projectionDto = projectionService.deleteProjection(ID);

    assertEquals(expected, projectionDto);
  }

  @Test(expected = ProgramNotFoundException.class)
  public void testDeleteProjection_projectionNotFound_throwsProgramNotFoundException() {
    when(projectionRepository.findById(anyInt())).thenReturn(Optional.empty());

    projectionService.deleteProjection(ID);
  }

  @Test
  public void testIsHallAvailableInPeriod_overlappingProjection_returnsFalse() {
    int hallId = 1;
    int programId = 1;
    LocalTime startTime = LocalTime.of(21, 0);

    Projection overlappingProjection = new Projection();
    overlappingProjection.setStartTime(LocalTime.of(23, 0));
    Program program = new Program();
    program.setId(programId);
    overlappingProjection.setProgram(program);

    when(projectionRepository.findProjectionsByHallIdAndStartTimeBetween(anyInt(), any(), any())).thenReturn(
      Collections.singletonList(overlappingProjection));

    boolean isAvailable = projectionService.isHallAvailable(hallId, programId, startTime);

    assertFalse(isAvailable);
  }

  @Test
  public void testIsHallAvailableInPeriod_noOverlappingProjection_returnsTrue() {
    int hallId = 1;
    int programId = 1;
    LocalTime startTime = LocalTime.of(10, 0);

    boolean isAvailable = projectionService.isHallAvailable(hallId, programId, startTime);

    assertTrue(isAvailable);
  }

  @Test
  public void testIsHallAvailable_crossMidnight_overlappingProjectionInMorning_returnsFalse() {
    int hallId = 1;
    int programId = 1;
    LocalTime startTime = LocalTime.of(23, 0);

    Projection overlappingProjection = new Projection();
    overlappingProjection.setStartTime(LocalTime.of(1, 0));
    Program program = new Program();
    program.setId(programId);
    overlappingProjection.setProgram(program);

    when(projectionRepository.findProjectionsByHallIdAndStartTimeBetween(anyInt(), any(), any())).thenReturn(
      Collections.emptyList());
    when(projectionRepository.findProjectionsByHallIdAndStartTimeBetween(anyInt(), any(), any()))
      .thenReturn(Collections.singletonList(overlappingProjection));

    boolean isAvailable = projectionService.isHallAvailable(hallId, programId, startTime);

    assertFalse(isAvailable);
  }

  @Test
  public void testIsHallAvailable_crossMidnight_noOverlappingProjection_returnsTrue() {
    int hallId = 1;
    int programId = 1;
    LocalTime startTime = LocalTime.of(23, 0);

    boolean isAvailable = projectionService.isHallAvailable(hallId, programId, startTime);

    assertTrue(isAvailable);
  }
}
