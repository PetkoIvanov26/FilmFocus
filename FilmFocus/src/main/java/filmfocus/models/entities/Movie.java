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
import java.time.Duration;
import java.time.LocalDate;

@Entity
@Table(name = "movies")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Movie {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column
  private String title;

  @Column(name = "average_rating")
  private double averageRating;

  @Column
  private String description;

  @Column(name = "release_date")
  private LocalDate releaseDate;

  @Column
  private Duration runtime;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  public Movie(String title, String description, LocalDate releaseDate, Duration runtime, Category category) {
    this.title = title;
    this.description = description;
    this.releaseDate = releaseDate;
    this.runtime = runtime;
    this.category = category;
  }

  public Movie(int id, String title, String description, LocalDate releaseDate, Duration runtime, Category category) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.releaseDate = releaseDate;
    this.runtime = runtime;
    this.category = category;
  }
}
