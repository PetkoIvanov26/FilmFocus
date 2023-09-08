package filmfocus.repositories;

import filmfocus.models.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

  Optional<Movie> findByTitle(String title);

  List<Movie> findByTitleContaining(String title);

  List<Movie> findByTitleContainingAndAverageRatingGreaterThanEqual(String title, double minRating);

  List<Movie> findByAverageRatingGreaterThanEqual(double averageRating);

  List<Movie> findByCategoryId(int categoryId);

  List<Movie> findByCategoryIdAndAverageRatingGreaterThanEqual(int categoryId, double rating);

  List<Movie> findByReleaseDateAfter(LocalDate date);

  List<Movie> findByReleaseDateBefore(LocalDate date);

  List<Movie> findByReleaseDateAfterAndAverageRatingGreaterThanEqual(LocalDate date, double rating);

  List<Movie> findByReleaseDateBeforeAndAverageRatingGreaterThanEqual(LocalDate date, double rating);
}
