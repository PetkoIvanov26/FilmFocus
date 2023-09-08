package filmfocus.mappers;

import filmfocus.models.dtos.UserDto;
import filmfocus.models.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

  private static final Logger log = LoggerFactory.getLogger(UserMapper.class);

  private final RoleMapper roleMapper;

  @Autowired
  public UserMapper(RoleMapper roleMapper) {
    this.roleMapper = roleMapper;
  }

  public UserDto mapUserToUserDto(User user) {
    log.info(String.format("The user with username %s is being mapped to a user DTO", user.getUsername()));
    return new UserDto(user.getId(), user.getUsername(),
                       user.getEmail(), user.getFirstName(),
                       user.getLastName(), user.getJoinDate(),
                       roleMapper.mapRolesToRoleDtos(user.getRoles()));
  }

  public List<UserDto> mapUsersToUserDtos(List<User> users) {
    return users.stream()
                .map(this::mapUserToUserDto)
                .collect(Collectors.toList());
  }
}