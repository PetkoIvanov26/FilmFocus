package filmfocus.services;

import filmfocus.exceptions.DateNotValidException;
import filmfocus.exceptions.ProgramAlreadyExistsException;
import filmfocus.exceptions.ProgramNotFoundException;
import filmfocus.mappers.ProgramMapper;
import filmfocus.models.dtos.ProgramDto;
import filmfocus.models.entities.Cinema;
import filmfocus.models.entities.Program;
import filmfocus.models.requests.ProgramRequest;
import filmfocus.repositories.ProgramRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static filmfocus.utils.constants.ExceptionMessages.DATE_NOT_VALID_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.ITEM_ALREADY_EXISTS_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.PROGRAM_ALREADY_EXISTS_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.PROGRAM_NOT_FOUND_MESSAGE;

@Service
public class ProgramService {

  private static final Logger log = LoggerFactory.getLogger(ProgramService.class);

  private final ProgramRepository programRepository;
  private final ProgramMapper programMapper;
  private final CinemaService cinemaService;

  @Autowired
  public ProgramService(ProgramRepository programRepository, ProgramMapper programMapper, CinemaService cinemaService) {
    this.programRepository = programRepository;
    this.programMapper = programMapper;
    this.cinemaService = cinemaService;
  }

  public Program addProgram(ProgramRequest request) {
    if (isDateNotValid(request.getProgramDate())) {
      log.error(String.format("Exception caught: %s", DATE_NOT_VALID_MESSAGE));

      throw new DateNotValidException(DATE_NOT_VALID_MESSAGE);
    }

    programRepository.findByProgramDateAndCinemaId(request.getProgramDate(), request.getCinemaId())
                     .ifPresent(existingProgram -> {

                       log.error(String.format("Exception caught: %s", PROGRAM_ALREADY_EXISTS_MESSAGE));
                       throw new ProgramAlreadyExistsException(PROGRAM_ALREADY_EXISTS_MESSAGE);
                     });

    log.info("An attempt to add a new program in the database");

    return programRepository.save(
      new Program(request.getProgramDate(), cinemaService.getCinemaById(request.getCinemaId())));
  }

  public List<ProgramDto> getAllPrograms(LocalDate date) {
    if (Objects.nonNull(date)) {
      log.info(String.format("All programs with date %s were requested from the database", date));

      return programMapper.mapProgramListToProgramDtoList(programRepository.findAllByProgramDate(date));
    } else {
      log.info("All programs were requested from the database");

      return programMapper.mapProgramListToProgramDtoList(programRepository.findAll());
    }
  }

  public List<ProgramDto> getProgramsByCinemaId(int cinemaId) {
    log.info(String.format("All programs with cinema id %d were requested from the database", cinemaId));

    Cinema cinema = cinemaService.getCinemaById(cinemaId);

    return programMapper.mapProgramListToProgramDtoList(programRepository.findAllByCinemaId(cinema.getId()));
  }

  public Program getProgramById(int id) {
    log.info(String.format("An attempt to extract a program with an id %d from the database", id));

    return programRepository.findById(id).orElseThrow(() -> {

      log.error(String.format("Exception caught: %s", PROGRAM_NOT_FOUND_MESSAGE));

      throw new ProgramNotFoundException(PROGRAM_NOT_FOUND_MESSAGE);
    });
  }

  public ProgramDto getProgramDtoById(int id) {
    log.info(String.format("An attempt to extract a program DTO with an id %d from the database", id));

    return programMapper.mapProgramToProgramDto(getProgramById(id));
  }

  public ProgramDto updateProgram(ProgramRequest request, int programId) {
    ProgramDto programDto = getProgramDtoById(programId);

    if (isDateNotValid(request.getProgramDate())) {

      log.error(String.format("Exception caught: %s", DATE_NOT_VALID_MESSAGE));

      throw new DateNotValidException(DATE_NOT_VALID_MESSAGE);
    }

    programRepository.save(new Program(programId, request.getProgramDate(),
                                       cinemaService.getCinemaById(request.getCinemaId())));

    log.info(String.format("Program with an id %d has been updated", programId));

    return programDto;
  }

  public ProgramDto deleteProgram(int programId) {
    ProgramDto programDto = getProgramDtoById(programId);

    programRepository.deleteById(programId);

    log.info(String.format("Program with an id %d has been deleted", programId));

    return programDto;
  }

  public boolean isDateNotValid(LocalDate date) {
    return date.isBefore(LocalDate.now());
  }
}