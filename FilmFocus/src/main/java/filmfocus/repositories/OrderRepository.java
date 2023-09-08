package filmfocus.repositories;

import filmfocus.models.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

  List<Order> findOrderByUserId(int userId);

  List<Order> findOrdersByDateOfPurchaseBetween(LocalDate startDate, LocalDate endDate);
}
