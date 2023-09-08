package filmfocus.jwt;

import filmfocus.exceptions.UserNotFoundException;
import filmfocus.models.entities.Role;
import filmfocus.models.entities.User;
import filmfocus.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static filmfocus.utils.constants.ExceptionMessages.USER_NOT_FOUND_MESSAGE;

@Service
public class JwtUserDetailsService implements UserDetailsService {

  private final Logger log = LoggerFactory.getLogger(JwtUserDetailsService.class);

  private final UserRepository userRepository;

  @Autowired
  public JwtUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user =
      userRepository.findUserByUsername(username).orElseThrow(() -> {
        log.error(USER_NOT_FOUND_MESSAGE);
        throw new UserNotFoundException(USER_NOT_FOUND_MESSAGE);
      });

    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    for (Role role : user.getRoles()) {
      authorities.add(new SimpleGrantedAuthority(role.getName()));
    }

    log.info(String.format("User with username %s loaded", username));
    return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
  }
}