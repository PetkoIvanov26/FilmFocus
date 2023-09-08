package filmfocus.services;

import filmfocus.exceptions.RoleAlreadyExistsException;
import filmfocus.exceptions.RoleNotFoundException;
import filmfocus.mappers.RoleMapper;
import filmfocus.models.dtos.RoleDto;
import filmfocus.models.entities.Role;
import filmfocus.models.requests.RoleRequest;
import filmfocus.repositories.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static filmfocus.utils.constants.ExceptionMessages.ROLE_ALREADY_EXISTS_MESSAGE;
import static filmfocus.utils.constants.ExceptionMessages.ROLE_NOT_FOUND_MESSAGE;

@Service
public class RoleService {

  private static final Logger log = LoggerFactory.getLogger(RoleService.class);

  private final RoleRepository roleRepository;
  private final RoleMapper roleMapper;

  @Autowired
  public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
    this.roleRepository = roleRepository;
    this.roleMapper = roleMapper;
  }

  public Role addRole(RoleRequest roleRequest) {
    log.info("Trying to add a new role");

    roleValidation(roleRequest);

    return roleRepository.save(new Role(roleRequest.getName()));
  }

  public List<Role> getAllRoles() {
    log.info("Trying to retrieve all roles");

    return roleRepository.findAll();
  }

  public List<RoleDto> getAllRolesDto() {
    log.info("Trying to retrieve all roles DTOs");

    return roleMapper.mapRolesToRoleDtos(getAllRoles());
  }

  public Role getRoleById(int id) {
    log.info(String.format("Trying to retrieve role with id %d", id));

    return roleRepository.findById(id).orElseThrow(() -> {
      log.error(String.format("Exception caught: %s", ROLE_NOT_FOUND_MESSAGE));

      throw new RoleNotFoundException(ROLE_NOT_FOUND_MESSAGE);
    });
  }

  public RoleDto getRoleDtoById(int id) {
    log.info(String.format("Trying to retrieve role DTO with id %d", id));

    return roleMapper.mapRoleToRoleDto(getRoleById(id));
  }

  public Role getRoleByName(String name) {
    log.info(String.format("Trying to retrieve role with name %s", name));

    return roleRepository.findRoleByName(name.toUpperCase()).orElseThrow(() -> {
      log.error(String.format("Exception caught: %s", ROLE_NOT_FOUND_MESSAGE));

      throw new RoleNotFoundException(ROLE_NOT_FOUND_MESSAGE);
    });
  }

  public RoleDto getRoleDtoByName(String name) {
    log.info(String.format("Trying to retrieve role DTO with name %s", name));

    return roleMapper.mapRoleToRoleDto(getRoleByName(name));
  }

  public RoleDto updateRole(RoleRequest roleRequest, int id) {
    RoleDto roleDto = getRoleDtoById(id);

    roleRepository.save(new Role(id, roleRequest.getName()));

    log.info(String.format("Role with id %d was updated", id));

    return roleDto;
  }

  public RoleDto deleteRole(int id) {
    RoleDto roleDto = getRoleDtoById(id);

    roleRepository.deleteById(id);

    log.info(String.format("Role with id %d was deleted", id));

    return roleDto;
  }

  private void roleValidation(RoleRequest roleRequest) {
    roleRepository.findRoleByName(roleRequest.getName()).ifPresent(role -> {
      log.error(String.format("Error caught: %s", ROLE_ALREADY_EXISTS_MESSAGE));

      throw new RoleAlreadyExistsException(ROLE_ALREADY_EXISTS_MESSAGE);
    });
  }
}