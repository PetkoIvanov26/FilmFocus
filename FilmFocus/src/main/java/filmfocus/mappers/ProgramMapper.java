package filmfocus.mappers;

import filmfocus.models.dtos.ProgramDto;
import filmfocus.models.entities.Program;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProgramMapper {

  private static final Logger log = LoggerFactory.getLogger(ProgramMapper.class);

  private final CinemaMapper cinemaMapper;

  @Autowired
  public ProgramMapper(CinemaMapper cinemaMapper) {
    this.cinemaMapper = cinemaMapper;
  }

  public ProgramDto mapProgramToProgramDto(Program program) {
    log.info(String.format("The program with an id %d is being mapped to a program DTO", program.getId()));
    return new ProgramDto(program.getId(),
                          program.getProgramDate(),
                          cinemaMapper.mapCinemaToCinemaDto(program.getCinema()));
  }

  public List<ProgramDto> mapProgramListToProgramDtoList(List<Program> programs) {
    return programs.stream()
                   .map(this::mapProgramToProgramDto)
                   .collect(Collectors.toList());
  }
}
