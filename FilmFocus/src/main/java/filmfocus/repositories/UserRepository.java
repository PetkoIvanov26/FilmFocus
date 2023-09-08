package filmfocus.repositories;

import filmfocus.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findUserByUsername(String username);

  Optional<User> findUserByEmail(String email);

  List<User> findAllByRolesName(String roleName);

  List<User> findAllByJoinDateBefore(LocalDate dateBefore);

  List<User> findAllByJoinDateAfter(LocalDate dateAfter);
}