package filmfocus.controllers;

import filmfocus.models.dtos.CinemaDto;
import filmfocus.models.entities.Cinema;
import filmfocus.models.requests.CinemaRequest;
import filmfocus.services.CinemaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static filmfocus.utils.constants.URIConstants.CINEMAS_ID_PATH;
import static filmfocus.utils.constants.URIConstants.CINEMAS_PATH;

@RestController
public class CinemaController {

  private static final Logger log = LoggerFactory.getLogger(CinemaController.class);

  private final CinemaService cinemaService;

  @Autowired
  public CinemaController(CinemaService cinemaService) {
    this.cinemaService = cinemaService;
  }

  @GetMapping(CINEMAS_PATH)
  public ResponseEntity<List<CinemaDto>> getCinemas(
    @RequestParam(required = false) String city,
    @RequestParam(required = false) String address) {

    List<CinemaDto> cinemaDtos = cinemaService.getAllCinemas(city, address);
    log.info("All cinemas were requested from the database");

    return ResponseEntity.ok(cinemaDtos);
  }

  @PostMapping(CINEMAS_PATH)
  public ResponseEntity<Void> addCinema(@RequestBody @Valid CinemaRequest request) {
    Cinema cinema = cinemaService.addCinema(request);
    log.info("A request for a cinema to be added has been submitted");

    URI location = UriComponentsBuilder
      .fromUriString(CINEMAS_ID_PATH)
      .buildAndExpand(cinema.getId())
      .toUri();

    return ResponseEntity.created(location).build();
  }

  @PutMapping(CINEMAS_ID_PATH)
  public ResponseEntity<CinemaDto> updateCinema(
    @RequestBody @Valid CinemaRequest request, @PathVariable int id,
    @RequestParam(required = false) boolean returnOld) {

    CinemaDto cinemaDto = cinemaService.updateCinema(request, id);
    log.info(String.format("Cinema with id %d was updated", id));

    return returnOld ? ResponseEntity.ok(cinemaDto) : ResponseEntity.noContent().build();
  }

  @DeleteMapping(CINEMAS_ID_PATH)
  public ResponseEntity<CinemaDto> deleteCinema(
    @PathVariable int id, @RequestParam(required = false) boolean returnOld) {

    CinemaDto cinemaDto = cinemaService.deleteCinema(id);
    log.info(String.format("Cinema with id %d was deleted", id));

    return returnOld ? ResponseEntity.ok(cinemaDto) : ResponseEntity.noContent().build();
  }
}
