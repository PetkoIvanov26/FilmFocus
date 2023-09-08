package filmfocus.controllers;

import filmfocus.models.dtos.RoleDto;
import filmfocus.models.entities.Role;
import filmfocus.models.requests.RoleRequest;
import filmfocus.services.RoleService;
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

import static filmfocus.utils.constants.URIConstants.ROLES_ID_PATH;
import static filmfocus.utils.constants.URIConstants.ROLES_PATH;

@RestController
public class RoleController {

  private static final Logger log = LoggerFactory.getLogger(RoleController.class);

  private final RoleService roleService;

  @Autowired
  public RoleController(RoleService roleService) {
    this.roleService = roleService;
  }

  @PostMapping(ROLES_PATH)
  public ResponseEntity<Void> addRole(@RequestBody @Valid RoleRequest request) {
    Role role = roleService.addRole(request);
    log.info("A request for a user role to be added has been submitted");

    URI location = UriComponentsBuilder
      .fromUriString(ROLES_ID_PATH)
      .buildAndExpand(role.getId())
      .toUri();

    return ResponseEntity.created(location).build();
  }

  @GetMapping(ROLES_PATH)
  public ResponseEntity<List<RoleDto>> getAllRoles() {
    List<RoleDto> roleDtos = roleService.getAllRolesDto();
    log.info("All user roles were requested from the database");

    return ResponseEntity.ok(roleDtos);
  }

  @GetMapping(value = ROLES_PATH, params = "name")
  public ResponseEntity<RoleDto> getRoleByName(@RequestParam String name) {
    RoleDto roleDto = roleService.getRoleDtoByName(name);
    log.info("User role by name was requested from the database");

    return ResponseEntity.ok(roleDto);
  }

  @PutMapping(ROLES_ID_PATH)
  public ResponseEntity<RoleDto> updateRole(
    @RequestBody @Valid RoleRequest request, @PathVariable int id, @RequestParam(required = false) boolean returnOld) {

    RoleDto roleDto = roleService.updateRole(request, id);
    log.info(String.format("User role with id %d was updated", id));

    return returnOld ? ResponseEntity.ok(roleDto) : ResponseEntity.noContent().build();
  }

  @DeleteMapping(ROLES_ID_PATH)
  public ResponseEntity<RoleDto> deleteRole(@PathVariable int id, @RequestParam(required = false) boolean returnOld) {
    RoleDto roleDto = roleService.deleteRole(id);
    log.info(String.format("User role with id %d was deleted", id));

    return returnOld ? ResponseEntity.ok(roleDto) : ResponseEntity.noContent().build();
  }
}