package filmfocus.services;

import filmfocus.exceptions.CinemaAlreadyExistsException;
import filmfocus.exceptions.CinemaNotFoundException;
import filmfocus.mappers.CinemaMapper;
import filmfocus.models.dtos.CinemaDto;
import filmfocus.models.entities.Cinema;
import filmfocus.models.requests.CinemaRequest;
import filmfocus.repositories.CinemaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static filmfocus.utils.constants.ExceptionMessages.CINEMA_ALREADY_EXISTS_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.CINEMA_NOT_FOUND_MESSAGE;

@Service
public class CinemaService {

  private static final Logger log = LoggerFactory.getLogger(CinemaService.class);

  private final CinemaRepository cinemaRepository;
  private final CinemaMapper cinemaMapper;

  @Autowired
  public CinemaService(CinemaRepository cinemaRepository, CinemaMapper cinemaMapper) {
    this.cinemaRepository = cinemaRepository;
    this.cinemaMapper = cinemaMapper;
  }

  public Cinema addCinema(CinemaRequest cinemaRequest) {
    log.info("An attempt to add a new cinema in the database");

    cinemaValidation(cinemaRequest);

    return cinemaRepository.save(new Cinema(cinemaRequest.getAddress(), cinemaRequest.getCity(), 0));
  }

  public List<CinemaDto> getAllCinemas(String city, String address) {
    if (Objects.isNull(city) && Objects.isNull(address)) {

      log.error("Exception caught: You haven't provided any query parameters ");

      throw new IllegalArgumentException("You haven't provided any query parameters");
    } else if (Objects.nonNull(city) && Objects.nonNull(address)) {

      log.info(
        String.format("All cinemas with city %s and address %s were requested from the database", city, address));

      return cinemaMapper.mapCinemaToCinemaDtoList(cinemaRepository.findAllByCityAndAddress(city, address));
    } else if (Objects.nonNull(city)) {

      log.info(String.format("All cinemas with city %s were requested from the database", city));

      return cinemaMapper.mapCinemaToCinemaDtoList(cinemaRepository.findAllByCity(city));
    } else {

      log.info(String.format("All cinemas with address %s were requested from the database", address));

      return cinemaMapper.mapCinemaToCinemaDtoList(cinemaRepository.findAllByAddress(address));
    }
  }

  public Cinema getCinemaById(int id) {
    log.info(String.format("An attempt to extract a cinema with id %d from the database", id));

    return cinemaRepository.findById(id).orElseThrow(() -> {

      log.error(String.format("Exception caught : %s", CINEMA_NOT_FOUND_MESSAGE));

      throw new CinemaNotFoundException(CINEMA_NOT_FOUND_MESSAGE);
    });
  }

  public CinemaDto getCinemaDtoById(int id) {
    log.info(String.format("An attempt to extract a cinemaDto with id %d from the database", id));

    return cinemaMapper.mapCinemaToCinemaDto(getCinemaById(id));
  }

  public CinemaDto updateCinema(CinemaRequest cinemaRequest, int id) {
    CinemaDto cinemaDto = getCinemaDtoById(id);

    cinemaRepository.save(
      new Cinema(id, cinemaRequest.getAddress(), cinemaRequest.getCity(), cinemaDto.getAverageRating()));

    log.info(String.format("Cinema with an id %d has been updated", id));

    return cinemaDto;
  }

  public CinemaDto updateCinemaAverageRating(double newRating, int cinemaId) {
    Cinema cinema = getCinemaById(cinemaId);
    CinemaDto cinemaDto = cinemaMapper.mapCinemaToCinemaDto(cinema);
    cinema.setAverageRating(newRating);
    cinemaDto.setAverageRating(newRating);

    cinemaRepository.save(cinema);

    log.info(String.format("Updated average rating for cinema with id %d", cinemaId));

    return cinemaDto;
  }

  public CinemaDto deleteCinema(int id) {
    CinemaDto cinemaDto = getCinemaDtoById(id);

    cinemaRepository.deleteById(id);

    log.info(String.format("Cinema with an id %d has been deleted", id));

    return cinemaDto;
  }

  private void cinemaValidation(CinemaRequest cinemaRequest) {
    cinemaRepository.findByCityAndAddress(cinemaRequest.getCity(), cinemaRequest.getAddress()).ifPresent(cinema -> {
      log.error(String.format("Exception caught: %s", CINEMA_ALREADY_EXISTS_MESSAGE));
      throw new CinemaAlreadyExistsException(CINEMA_ALREADY_EXISTS_MESSAGE);
    });
  }
}
