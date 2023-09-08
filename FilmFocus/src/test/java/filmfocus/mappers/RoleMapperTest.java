package filmfocus.mappers;

import filmfocus.models.dtos.RoleDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static filmfocus.testUtils.constants.RoleConstants.ID;
import static filmfocus.testUtils.constants.RoleConstants.NAME;
import static filmfocus.testUtils.factories.RoleFactory.getDefaultRole;
import static filmfocus.testUtils.factories.RoleFactory.getDefaultRoleList;

@RunWith(MockitoJUnitRunner.class)
public class RoleMapperTest {

    @InjectMocks
    private RoleMapper roleMapper;

    @Test
    public void testMapRoleToRoleDto_success() {
        RoleDto roleDto = roleMapper.mapRoleToRoleDto(getDefaultRole());

        Assert.assertEquals(ID, roleDto.getId());
        Assert.assertEquals(NAME, roleDto.getName());
    }

    @Test
    public void testMapRolesToRoleDtos_success() {
        List<RoleDto> result = roleMapper.mapRolesToRoleDtos(getDefaultRoleList());

        Assert.assertEquals(ID, result.get(0).getId());
        Assert.assertEquals(NAME, result.get(0).getName());
    }
}
