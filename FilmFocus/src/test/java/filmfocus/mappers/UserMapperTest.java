package filmfocus.mappers;

import filmfocus.models.dtos.UserDto;
import filmfocus.models.entities.User;
import filmfocus.testUtils.factories.RoleFactory;
import filmfocus.testUtils.factories.UserFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static filmfocus.testUtils.constants.UserConstants.EMAIL;
import static filmfocus.testUtils.constants.UserConstants.FIRST_NAME;
import static filmfocus.testUtils.constants.UserConstants.JOIN_DATE;
import static filmfocus.testUtils.constants.UserConstants.LAST_NAME;
import static filmfocus.testUtils.constants.UserConstants.USERNAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserMapperTest {

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private UserMapper userMapper;

    @Test
    public void testMapUsersToUserDtos_success() {
        List<User> users = UserFactory.getDefaultUserList();
        List<UserDto> expectedDtoList = UserFactory.getDefaultUserDtoList();
        when(roleMapper.mapRolesToRoleDtos(any())).thenReturn(RoleFactory.getDefaultRoleDtoList());

        List<UserDto> actualDtoList = userMapper.mapUsersToUserDtos(users);

        Assert.assertEquals(expectedDtoList.size(), actualDtoList.size());

        for (int i = 0; i < expectedDtoList.size(); i++) {
            UserDto expectedDto = expectedDtoList.get(i);
            UserDto actualDto = actualDtoList.get(i);

            Assert.assertEquals(expectedDto.getId(), actualDto.getId());
            Assert.assertEquals(expectedDto.getUsername(), actualDto.getUsername());
            Assert.assertEquals(expectedDto.getEmail(), actualDto.getEmail());
            Assert.assertEquals(expectedDto.getFirstName(), actualDto.getFirstName());
            Assert.assertEquals(expectedDto.getLastName(), actualDto.getLastName());
            Assert.assertEquals(expectedDto.getJoinDate(), actualDto.getJoinDate());
            Assert.assertEquals(expectedDto.getRoles().get(0).getId(), actualDto.getRoles().get(0).getId());
            Assert.assertEquals(expectedDto.getRoles().get(0).getName(), actualDto.getRoles().get(0).getName());
        }
    }

    @Test
    public void testMapUserToUserDto_success() {
        when(roleMapper.mapRolesToRoleDtos(any())).thenReturn(RoleFactory.getDefaultRoleDtoList());
        User user = UserFactory.getDefaultUser();

        UserDto actualDto = userMapper.mapUserToUserDto(user);

        Assert.assertEquals(USERNAME, actualDto.getUsername());
        Assert.assertEquals(EMAIL, actualDto.getEmail());
        Assert.assertEquals(FIRST_NAME, actualDto.getFirstName());
        Assert.assertEquals(LAST_NAME, actualDto.getLastName());
        Assert.assertEquals(JOIN_DATE, actualDto.getJoinDate());
        Assert.assertEquals(RoleFactory.getDefaultRoleDtoList().get(0).getName(), actualDto.getRoles().get(0).getName());
    }
}
