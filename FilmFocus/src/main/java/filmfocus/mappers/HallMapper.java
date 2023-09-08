package filmfocus.mappers;

import filmfocus.models.dtos.HallDto;
import filmfocus.models.entities.Hall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HallMapper {

  private static final Logger log = LoggerFactory.getLogger(HallMapper.class);

  private final CinemaMapper cinemaMapper;

  @Autowired
  public HallMapper(CinemaMapper cinemaMapper) {
    this.cinemaMapper = cinemaMapper;
  }

  public HallDto mapHallToHallDto(Hall hall) {
    log.info(String.format("The hall with an id %d is being mapped to a hall DTO", hall.getId()));
    return new HallDto(hall.getId(), hall.getCapacity(), cinemaMapper.mapCinemaToCinemaDto(hall.getCinema()));
  }

  public List<HallDto> mapHallListToHallDtoList(List<Hall> halls) {
    return halls.stream()
                .map(this::mapHallToHallDto)
                .collect(Collectors.toList());
  }
}