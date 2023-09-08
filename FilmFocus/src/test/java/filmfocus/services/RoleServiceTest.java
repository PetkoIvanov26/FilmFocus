package filmfocus.services;

import filmfocus.exceptions.RoleAlreadyExistsException;
import filmfocus.exceptions.RoleNotFoundException;
import filmfocus.mappers.RoleMapper;
import filmfocus.models.dtos.RoleDto;
import filmfocus.models.entities.Role;
import filmfocus.repositories.RoleRepository;
import filmfocus.testUtils.factories.RoleFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static filmfocus.testUtils.constants.RoleConstants.ID;
import static filmfocus.testUtils.constants.RoleConstants.NAME;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceTest {

  @Mock
  private RoleRepository roleRepository;

  @Mock
  private RoleMapper roleMapper;

  @InjectMocks
  private RoleService roleService;

  @Test
  public void testAddRole_noExceptions_success() {
    Role expected = RoleFactory.getDefaultRole();

    when(roleRepository.save(any())).thenReturn(expected);

    Role role = roleService.addRole(RoleFactory.getDefaultRoleRequest());

    assertEquals(expected, role);
  }

  @Test
  public void testGetAllRoles_rolesFound_success() {
    List<Role> expected = RoleFactory.getDefaultRoleList();

    when(roleRepository.findAll()).thenReturn(expected);

    List<Role> roles = roleService.getAllRoles();

    assertEquals(expected, roles);
  }

  @Test
  public void testGetAllRolesDto_rolesFound_success() {
    List<RoleDto> expected = RoleFactory.getDefaultRoleDtoList();

    when(roleRepository.findAll()).thenReturn(RoleFactory.getDefaultRoleList());
    when(roleMapper.mapRolesToRoleDtos(any())).thenReturn(expected);

    List<RoleDto> roles = roleService.getAllRolesDto();

    assertEquals(expected, roles);
  }

  @Test
  public void testGetRoleById_roleFound_success() {
    Role expected = RoleFactory.getDefaultRole();

    when(roleRepository.findById(anyInt())).thenReturn(Optional.of(expected));

    Role role = roleService.getRoleById(ID);

    assertEquals(expected, role);
  }

  @Test(expected = RoleNotFoundException.class)
  public void testGetRoleById_roleNotFound_throwsRoleNotFoundException() {
    roleService.getRoleById(ID);
  }

  @Test
  public void testGetRoleDtoById_roleDtoFound_success() {
    RoleDto expected = RoleFactory.getDefaultRoleDto();

    when(roleMapper.mapRoleToRoleDto(any())).thenReturn(expected);
    when(roleRepository.findById(anyInt())).thenReturn(Optional.of(RoleFactory.getDefaultRole()));

    RoleDto role = roleService.getRoleDtoById(ID);

    assertEquals(expected, role);
  }

  @Test
  public void testGetRoleByName_roleFound_success() {
    Role expected = RoleFactory.getDefaultRole();

    when(roleRepository.findRoleByName(anyString())).thenReturn(Optional.of(expected));

    Role role = roleService.getRoleByName(NAME);

    assertEquals(expected, role);
  }

  @Test(expected = RoleNotFoundException.class)
  public void testGetRoleByName_roleNotFound_throwRoleNotFoundException() {
    when(roleRepository.findRoleByName(anyString())).thenReturn(Optional.empty());

    roleService.getRoleByName(NAME);
  }

  @Test
  public void testGetRoleDtoByName_roleDtoFound_success() {
    RoleDto expected = RoleFactory.getDefaultRoleDto();

    when(roleMapper.mapRoleToRoleDto(any())).thenReturn(expected);
    when(roleRepository.findRoleByName(anyString())).thenReturn(Optional.of(RoleFactory.getDefaultRole()));

    RoleDto role = roleService.getRoleDtoByName(NAME);

    assertEquals(expected, role);
  }

  @Test
  public void testUpdateRole_roleUpdated_success() {
    RoleDto expected = RoleFactory.getDefaultRoleDto();

    when(roleMapper.mapRoleToRoleDto(any())).thenReturn(expected);
    when(roleRepository.findById(anyInt())).thenReturn(Optional.of(new Role()));
    when(roleRepository.save(any())).thenReturn(RoleFactory.getDefaultRole());

    RoleDto role = roleService.updateRole(RoleFactory.getDefaultRoleRequest(), ID);

    assertEquals(expected, role);
  }

  @Test(expected = RoleAlreadyExistsException.class)
  public void testAddRole_roleExists_throwsRoleAlreadyExistsException() {
    RoleDto expected = RoleFactory.getDefaultRoleDto();
    
    when(roleRepository.findRoleByName(anyString())).thenReturn(Optional.of(new Role()));

    roleService.addRole(RoleFactory.getDefaultRoleRequest());
  }

  @Test
  public void testDeleteRole_roleDeleted_success() {
    RoleDto expected = RoleFactory.getDefaultRoleDto();

    when(roleMapper.mapRoleToRoleDto(any())).thenReturn(expected);
    when(roleRepository.findById(anyInt())).thenReturn(Optional.of(RoleFactory.getDefaultRole()));

    RoleDto role = roleService.deleteRole(ID);

    assertEquals(expected, role);
  }
}