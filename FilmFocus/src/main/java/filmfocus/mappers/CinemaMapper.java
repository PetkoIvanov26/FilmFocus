package filmfocus.mappers;

import filmfocus.models.dtos.CinemaDto;
import filmfocus.models.entities.Cinema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CinemaMapper {

  private static final Logger log = LoggerFactory.getLogger(CinemaMapper.class);

  public CinemaDto mapCinemaToCinemaDto(Cinema cinema) {
    log.info(String.format("The cinema with an id %d is being mapped to a cinema DTO", cinema.getId()));
    return new CinemaDto(cinema.getId(), cinema.getAddress(), cinema.getCity(), cinema.getAverageRating());
  }

  public List<CinemaDto> mapCinemaToCinemaDtoList(List<Cinema> cinemaList) {
    return cinemaList.stream()
                     .map(this::mapCinemaToCinemaDto)
                     .collect(Collectors.toList());
  }
}