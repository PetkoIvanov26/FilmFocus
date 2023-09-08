package filmfocus.controllers;

import filmfocus.models.dtos.ProjectionDto;
import filmfocus.models.entities.Projection;
import filmfocus.models.requests.ProjectionRequest;
import filmfocus.services.ProjectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import java.time.LocalTime;
import java.util.List;

import static filmfocus.utils.constants.URIConstants.MOVIES_ID_PROJECTIONS_PATH;
import static filmfocus.utils.constants.URIConstants.PROGRAMS_ID_PROJECTIONS_PATH;
import static filmfocus.utils.constants.URIConstants.PROJECTIONS_ID_PATH;
import static filmfocus.utils.constants.URIConstants.PROJECTIONS_PATH;

@RestController
public class ProjectionController {

  private static final Logger log = LoggerFactory.getLogger(ProjectionController.class);

  private final ProjectionService projectionService;

  @Autowired
  public ProjectionController(ProjectionService projectionService) {
    this.projectionService = projectionService;
  }

  @PostMapping(PROJECTIONS_PATH)
  public ResponseEntity<Void> addProjection(@RequestBody @Valid ProjectionRequest request) {
    Projection projection = this.projectionService.addProjection(request);
    log.info("A request for a projection to be added has been submitted");

    URI location = UriComponentsBuilder
      .fromUriString(PROJECTIONS_ID_PATH)
      .buildAndExpand(projection.getId())
      .toUri();

    return ResponseEntity.created(location).build();
  }

  @GetMapping(PROGRAMS_ID_PROJECTIONS_PATH)
  public ResponseEntity<List<ProjectionDto>> getProjectionsByProgramId(@PathVariable int id) {
    List<ProjectionDto> projectionsDtos = this.projectionService.getProjectionsByProgramId(id);
    log.info("All projections by program id were requested from the database");

    return ResponseEntity.ok(projectionsDtos);
  }

  @GetMapping(MOVIES_ID_PROJECTIONS_PATH)
  public ResponseEntity<List<ProjectionDto>> getProjectionsByMovieId(@PathVariable int id) {
    List<ProjectionDto> projectionsDtos = this.projectionService.getProjectionsByMovieId(id);
    log.info("All projections by movie id were requested from the database");

    return ResponseEntity.ok(projectionsDtos);
  }

  @GetMapping(PROJECTIONS_PATH)
  public ResponseEntity<List<ProjectionDto>> getProjectionsByStartTime(
    @RequestParam
    @DateTimeFormat(pattern = "HH:mm:ss")
    LocalTime startTime,
    @RequestParam boolean isBefore) {

    List<ProjectionDto> projectionDtos = this.projectionService.getProjectionsByStartTime(startTime, isBefore);
    log.info("All projections by start time were requested from the database");

    return ResponseEntity.ok(projectionDtos);
  }

  @PutMapping(PROJECTIONS_ID_PATH)
  public ResponseEntity<ProjectionDto> updateProjection(
    @RequestBody @Valid ProjectionRequest request,
    @PathVariable int id,
    @RequestParam(required = false) boolean returnOld) {

    ProjectionDto projectionDto = this.projectionService.updateProjection(request, id);
    log.info(String.format("Projection with id %d was updated", id));

    return returnOld ? ResponseEntity.ok(projectionDto) : ResponseEntity.noContent().build();
  }

  @DeleteMapping(PROJECTIONS_ID_PATH)
  public ResponseEntity<ProjectionDto> deleteProjection(
    @PathVariable int id,
    @RequestParam(required = false) boolean returnOld) {

    ProjectionDto projectionDto = this.projectionService.deleteProjection(id);
    log.info(String.format("Projection with id %d was deleted", id));

    return returnOld ? ResponseEntity.ok(projectionDto) : ResponseEntity.noContent().build();
  }
}
