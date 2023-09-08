package filmfocus.testUtils.factories;

import filmfocus.models.dtos.ProjectionDto;
import filmfocus.models.entities.Projection;
import filmfocus.models.requests.ProjectionRequest;

import java.util.Collections;
import java.util.List;

import static filmfocus.testUtils.constants.ProjectionConstants.ID;
import static filmfocus.testUtils.constants.ProjectionConstants.PRICE;
import static filmfocus.testUtils.constants.ProjectionConstants.START_TIME;
import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class ProjectionFactory {

  private ProjectionFactory() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }

  public static ProjectionRequest getDefaultProjectionRequest() {
    return new ProjectionRequest(PRICE, HallFactory.getDefaultHall().getId(),
                                 ProgramFactory.getDefaultProgram().getId(),
                                 MovieFactory.getDefaultMovie().getId(), START_TIME);
  }

  public static Projection getDefaultProjection() {
    return new Projection(ID, PRICE, HallFactory.getDefaultHall(), ProgramFactory.getDefaultProgram(),
                          MovieFactory.getDefaultMovie(), START_TIME);
  }

  public static List<Projection> getDefaultProjectionList() {
    return Collections.singletonList(getDefaultProjection());
  }

  public static ProjectionDto getDefaultProjectionDto() {
    return new ProjectionDto(ID, PRICE, HallFactory.getDefaultHallDto(), ProgramFactory.getDefaultProgramDto(),
                             MovieFactory.getDefaultMovieDto(), START_TIME);
  }

  public static List<ProjectionDto> getDefaultProjectionDtoList() {
    return Collections.singletonList(getDefaultProjectionDto());
  }
}