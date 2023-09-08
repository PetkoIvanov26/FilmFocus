package filmfocus.controllers;

import filmfocus.models.dtos.HallDto;
import filmfocus.models.entities.Hall;
import filmfocus.models.requests.HallRequest;
import filmfocus.services.HallService;
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

import static filmfocus.utils.constants.URIConstants.CINEMAS_ID_HALLS_PATH;
import static filmfocus.utils.constants.URIConstants.HALLS_ID_PATH;
import static filmfocus.utils.constants.URIConstants.HALLS_PATH;

@RestController
public class HallController {

  private static final Logger log = LoggerFactory.getLogger(HallController.class);

  private final HallService hallService;

  @Autowired
  public HallController(HallService hallService) {
    this.hallService = hallService;
  }

  @PostMapping(HALLS_PATH)
  public ResponseEntity<Void> addHall(@RequestBody @Valid HallRequest request) {
    Hall hall = hallService.addHall(request);
    log.info("A request for a hall to be added has been submitted");

    URI location = UriComponentsBuilder
      .fromUriString(HALLS_ID_PATH)
      .buildAndExpand(hall.getId())
      .toUri();

    return ResponseEntity.created(location).build();
  }

  @GetMapping(CINEMAS_ID_HALLS_PATH)
  public ResponseEntity<List<HallDto>> getHallsByCinemaId(@PathVariable int id) {
    List<HallDto> hallDtos = hallService.getHallsByCinemaId(id);
    log.info(String.format("All halls with cinema id %d were requested from the database", id));

    return ResponseEntity.ok(hallDtos);
  }

  @PutMapping(HALLS_ID_PATH)
  public ResponseEntity<HallDto> updateHall(
    @RequestBody @Valid HallRequest request, @PathVariable int id, @RequestParam(required = false) boolean returnOld) {
    HallDto hallDto = hallService.updateHall(request, id);
    log.info(String.format("Hall with id %d was updated", id));

    return returnOld ? ResponseEntity.ok(hallDto) : ResponseEntity.noContent().build();
  }

  @DeleteMapping(HALLS_ID_PATH)
  public ResponseEntity<HallDto> deleteHall(@PathVariable int id, @RequestParam(required = false) boolean returnOld) {
    HallDto hallDto = hallService.deleteHall(id);
    log.info(String.format("Hall with id %d was deleted", id));

    return returnOld ? ResponseEntity.ok(hallDto) : ResponseEntity.noContent().build();
  }
}