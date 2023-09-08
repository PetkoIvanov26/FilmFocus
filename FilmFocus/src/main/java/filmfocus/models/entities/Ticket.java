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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "tickets")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Ticket {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "date_of_purchase")
  private LocalDate dateOfPurchase;

  @ManyToOne
  @JoinColumn(name = "projection_id")
  private Projection projection;

  public Ticket(LocalDate dateOfPurchase, Projection projection) {
    this.dateOfPurchase = dateOfPurchase;
    this.projection = projection;
  }
}
