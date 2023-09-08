package filmfocus.testUtils.factories;

import filmfocus.models.dtos.ProgramDto;
import filmfocus.models.entities.Program;
import filmfocus.models.requests.ProgramRequest;
import filmfocus.testUtils.constants.CinemaConstants;

import java.util.Collections;
import java.util.List;

import static filmfocus.testUtils.constants.ProgramConstants.DATE;
import static filmfocus.testUtils.constants.ProgramConstants.ID;
import static filmfocus.utils.constants.ExceptionMessages.NON_INSTANTIABLE_CLASS_MESSAGE;

public final class ProgramFactory {

  private ProgramFactory() throws IllegalAccessException {
    throw new IllegalAccessException(NON_INSTANTIABLE_CLASS_MESSAGE);
  }

  public static Program getDefaultProgram() {
    return new Program(ID, DATE, CinemaFactory.getDefaultCinema());
  }

  public static ProgramDto getDefaultProgramDto() {
    return new ProgramDto(ID, DATE, CinemaFactory.getDefaultCinemaDto());
  }

  public static List<Program> getDefaultProgramList() {
    return Collections.singletonList(getDefaultProgram());
  }

  public static List<ProgramDto> getDefaultProgramDtoList() {
    return Collections.singletonList(getDefaultProgramDto());
  }

  public static ProgramRequest getDefaultProgramRequest() {
    return new ProgramRequest(DATE, CinemaConstants.ID);
  }
}
