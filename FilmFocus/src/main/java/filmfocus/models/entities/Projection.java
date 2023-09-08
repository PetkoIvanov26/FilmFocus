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
import java.time.LocalTime;

@Entity
@Table(name = "projections")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Projection {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column
  private double price;

  @ManyToOne
  @JoinColumn(name = "hall_id")
  private Hall hall;

  @ManyToOne
  @JoinColumn(name = "program_id")
  private Program program;

  @ManyToOne
  @JoinColumn(name = "movie_id")
  private Movie movie;

  @Column(name = "start_time")
  private LocalTime startTime;

  public Projection(double price, Hall hall, Program program, Movie movie, LocalTime startTime) {
    this.price = price;
    this.hall = hall;
    this.program = program;
    this.movie = movie;
    this.startTime = startTime;
  }
}
