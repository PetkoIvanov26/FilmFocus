package filmfocus.repositories;

import filmfocus.models.entities.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer> {

  boolean existsByType(String type);

  boolean existsByCode(String code);

  Optional<Discount> findByType(String type);

  Optional<Discount> findByCode(String code);
}