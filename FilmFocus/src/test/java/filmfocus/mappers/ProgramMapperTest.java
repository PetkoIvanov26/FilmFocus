package filmfocus.mappers;

import filmfocus.models.dtos.ProgramDto;
import filmfocus.testUtils.factories.CinemaFactory;
import filmfocus.testUtils.factories.ProgramFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static filmfocus.testUtils.constants.ProgramConstants.DATE;
import static filmfocus.testUtils.constants.ProgramConstants.ID;
import static filmfocus.testUtils.constants.ProgramConstants.PROGRAM_CINEMA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProgramMapperTest {

  @Mock
  private CinemaMapper cinemaMapper;
  @InjectMocks
  private ProgramMapper programMapper;

  @Test
  public void testMapProgramToProgramDtoTest_noExceptions_success() {
    when(cinemaMapper.mapCinemaToCinemaDto(any())).thenReturn(CinemaFactory.getDefaultCinemaDto());

    ProgramDto resultDto = programMapper.mapProgramToProgramDto(ProgramFactory.getDefaultProgram());

    assertEquals(ID, resultDto.getId());
    assertEquals(DATE, resultDto.getProgramDate());
    assertEquals(PROGRAM_CINEMA.getId(), resultDto.getCinema().getId());
    assertEquals(PROGRAM_CINEMA.getAddress(), resultDto.getCinema().getAddress());
    assertEquals(PROGRAM_CINEMA.getCity(), resultDto.getCinema().getCity());
  }

  @Test
  public void testMapProgramListToProgramDtoList_noExceptions_success() {
    when(cinemaMapper.mapCinemaToCinemaDto(any())).thenReturn(CinemaFactory.getDefaultCinemaDto());
    List<ProgramDto> resultList = programMapper.mapProgramListToProgramDtoList(ProgramFactory.getDefaultProgramList());

    ProgramDto resultDto = resultList.get(0);

    assertEquals(ID, resultDto.getId());
    assertEquals(DATE, resultDto.getProgramDate());
    assertEquals(PROGRAM_CINEMA.getId(), resultDto.getCinema().getId());
    assertEquals(PROGRAM_CINEMA.getAddress(), resultDto.getCinema().getAddress());
    assertEquals(PROGRAM_CINEMA.getCity(), resultDto.getCinema().getCity());
  }
}
