package filmfocus.services;

import filmfocus.exceptions.DateNotValidException;
import filmfocus.exceptions.ProgramAlreadyExistsException;
import filmfocus.exceptions.ProgramNotFoundException;
import filmfocus.mappers.ProgramMapper;
import filmfocus.models.dtos.ProgramDto;
import filmfocus.models.entities.Program;
import filmfocus.models.requests.ProgramRequest;
import filmfocus.repositories.ProgramRepository;
import filmfocus.testUtils.factories.CinemaFactory;
import filmfocus.testUtils.factories.ProgramFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static filmfocus.testUtils.constants.ProgramConstants.DATE;
import static filmfocus.testUtils.constants.ProgramConstants.ID;
import static filmfocus.testUtils.constants.ProgramConstants.PAST_DATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProgramServiceTest {

    @Mock
    private ProgramRepository programRepository;

    @Mock
    private ProgramMapper programMapper;

    @Mock
    private CinemaService cinemaService;

    @InjectMocks
    private ProgramService programService;

    @Test
    public void testAddProgram_noExceptions_success() {
        Program expected = ProgramFactory.getDefaultProgram();

        when(programRepository.save(any())).thenReturn(expected);
        when(cinemaService.getCinemaById(anyInt())).thenReturn(CinemaFactory.getDefaultCinema());

        Program program = programService.addProgram(ProgramFactory.getDefaultProgramRequest());

        assertEquals(expected, program);
    }

    @Test(expected = DateNotValidException.class)
    public void testAddProgram_dateNotPresent_throwsDateNotValidException() {
        ProgramRequest request = ProgramFactory.getDefaultProgramRequest();
        request.setProgramDate(PAST_DATE);

        programService.addProgram(request);
    }

    @Test(expected = ProgramAlreadyExistsException.class)
    public void testAddProgram_programAlreadyExists_throwsProgramAlreadyExistsException() {
        when(programRepository.findByProgramDateAndCinemaId(any(), anyInt())).thenReturn(
          Optional.of(ProgramFactory.getDefaultProgram()));

        programService.addProgram(ProgramFactory.getDefaultProgramRequest());
    }

    @Test
    public void testGetAllPrograms_dateNotNull_success() {
        List<ProgramDto> expected = ProgramFactory.getDefaultProgramDtoList();

        when(programMapper.mapProgramListToProgramDtoList(any())).thenReturn(ProgramFactory.getDefaultProgramDtoList());
        when(programRepository.findAllByProgramDate(any())).thenReturn(ProgramFactory.getDefaultProgramList());

        List<ProgramDto> resultList = programService.getAllPrograms(DATE);

        assertEquals(expected, resultList);
    }

    @Test
    public void testGetAllPrograms_dateNull_success() {
        List<ProgramDto> expected = ProgramFactory.getDefaultProgramDtoList();

        when(programMapper.mapProgramListToProgramDtoList(any())).thenReturn(expected);
        when(programRepository.findAll()).thenReturn(ProgramFactory.getDefaultProgramList());

        List<ProgramDto> resultList = programService.getAllPrograms(null);

        assertEquals(expected, resultList);
    }

    @Test
    public void testGetProgramsByCinemaId_cinemaFound_success() {
        List<ProgramDto> expected = ProgramFactory.getDefaultProgramDtoList();

        when(cinemaService.getCinemaById(anyInt())).thenReturn(CinemaFactory.getDefaultCinema());
        when(programMapper.mapProgramListToProgramDtoList(any())).thenReturn(expected);
        when(programRepository.findAllByCinemaId(anyInt())).thenReturn(ProgramFactory.getDefaultProgramList());

        List<ProgramDto> resultList = programService.getProgramsByCinemaId(ID);

        assertEquals(expected, resultList);
    }

    @Test
    public void testGetProgramById_programFound_success() {
        Program expected = ProgramFactory.getDefaultProgram();

        when(programRepository.findById(anyInt())).thenReturn(Optional.of(expected));

        Program program = programService.getProgramById(ID);

        assertEquals(expected, program);
    }

    @Test(expected = ProgramNotFoundException.class)
    public void testGetProgramById_programNotFound_throwsProgramNotFoundException() {
        programService.getProgramById(ID);
    }

    @Test
    public void testGetProgramDtoById_programDtoFound_success() {
        ProgramDto expected = ProgramFactory.getDefaultProgramDto();

        when(programMapper.mapProgramToProgramDto(any())).thenReturn(expected);
        when(programRepository.findById(anyInt())).thenReturn(Optional.of(new Program()));

        ProgramDto program = programService.getProgramDtoById(ID);

        assertEquals(expected, program);
    }

    @Test
    public void testUpdateProgram_programUpdated_success() {
        ProgramDto expected = ProgramFactory.getDefaultProgramDto();

        when(programMapper.mapProgramToProgramDto(any())).thenReturn(expected);
        when(programRepository.findById(anyInt())).thenReturn(Optional.of(ProgramFactory.getDefaultProgram()));
        when(programRepository.save(any())).thenReturn(ProgramFactory.getDefaultProgram());

        ProgramDto program = programService.updateProgram(ProgramFactory.getDefaultProgramRequest(), ID);

        assertEquals(expected, program);

    }

    @Test(expected = DateNotValidException.class)
    public void testUpdateProgram_dateNotPresent_throwsDateNotValidException() {
        ProgramRequest request = ProgramFactory.getDefaultProgramRequest();
        request.setProgramDate(PAST_DATE);

        when(programMapper.mapProgramToProgramDto(any())).thenReturn(ProgramFactory.getDefaultProgramDto());
        when(programRepository.findById(anyInt())).thenReturn(Optional.of(ProgramFactory.getDefaultProgram()));

        programService.updateProgram(request, ID);
    }

    @Test
    public void testDeleteProgram_programDeleted_success() {
        ProgramDto expected = ProgramFactory.getDefaultProgramDto();

        when(programMapper.mapProgramToProgramDto(any())).thenReturn(expected);
        when(programRepository.findById(anyInt())).thenReturn(Optional.of(ProgramFactory.getDefaultProgram()));

        ProgramDto program = programService.deleteProgram(ID);

        assertEquals(expected, program);
    }

    @Test
    public void testIsDateNotValid() {
        boolean result = programService.isDateNotValid(LocalDate.of(2000, 1, 1));

        assertTrue(result);
    }
}