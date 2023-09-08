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

@Entity
@Table(name = "halls")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Hall {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column
  private int capacity;

  @ManyToOne
  @JoinColumn(name = "cinema_id")
  private Cinema cinema;

  public Hall(int capacity, Cinema cinema) {
    this.capacity = capacity;
    this.cinema = cinema;
  }
}
