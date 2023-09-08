package filmfocus.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "date_of_purchase")
  private LocalDate dateOfPurchase;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany
  private List<Ticket> tickets;

  @ManyToMany
  @JoinTable(
    name = "orders_items",
    joinColumns = {@JoinColumn(name = "order_id")},
    inverseJoinColumns = {@JoinColumn(name = "item_id")})
  private List<Item> items;

  @Column(name = "total_price")
  private double totalPrice;

  public Order(LocalDate dateOfPurchase, User user, List<Ticket> tickets, List<Item> items, double totalPrice) {
    this.dateOfPurchase = dateOfPurchase;
    this.user = user;
    this.tickets = tickets;
    this.items = items;
    this.totalPrice = totalPrice;
  }
}
