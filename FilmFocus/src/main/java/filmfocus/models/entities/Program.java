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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "programs")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Program {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "program_date")
  private LocalDate programDate;

  @OneToOne
  @JoinColumn(name = "cinema_id")
  private Cinema cinema;

  public Program(LocalDate programDate, Cinema cinema) {
    this.programDate = programDate;
    this.cinema = cinema;
  }
}
