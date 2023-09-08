package filmfocus.mappers;

import filmfocus.models.dtos.ProjectionDto;
import filmfocus.models.entities.Projection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectionMapper {

  private static final Logger log = LoggerFactory.getLogger(ProjectionMapper.class);

  private final HallMapper hallMapper;
  private final ProgramMapper programMapper;
  private final MovieMapper movieMapper;

  @Autowired
  public ProjectionMapper(HallMapper hallMapper, ProgramMapper programMapper, MovieMapper movieMapper) {
    this.hallMapper = hallMapper;
    this.programMapper = programMapper;
    this.movieMapper = movieMapper;
  }

  public ProjectionDto mapProjectionToProjectionDto(Projection projection) {
    log.info(String.format("The projection with an id %d is being mapped to a projection DTO", projection.getId()));
    return new ProjectionDto(projection.getId(), projection.getPrice(),
                             hallMapper.mapHallToHallDto(projection.getHall()),
                             programMapper.mapProgramToProgramDto(projection.getProgram()),
                             movieMapper.mapMovieToMovieDto(projection.getMovie()),
                             projection.getStartTime());
  }

  public List<ProjectionDto> mapProjectionListToProjectionDtoList(List<Projection> projections) {
    return projections.stream()
                      .map(this::mapProjectionToProjectionDto)
                      .collect(Collectors.toList());
  }
}
