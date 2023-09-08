package filmfocus.repositories;

import filmfocus.models.entities.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Integer> {

  List<Cinema> findAllByCity(String city);

  List<Cinema> findAllByAddress(String address);

  List<Cinema> findAllByCityAndAddress(String city, String address);

  Optional<Cinema> findByCityAndAddress(String city, String address);
}