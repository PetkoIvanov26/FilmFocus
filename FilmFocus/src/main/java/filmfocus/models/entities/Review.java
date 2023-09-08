package filmfocus.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "reviews")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Review {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column
  private double rating;

  @Column(name = "review_text")
  private String reviewText;

  @Column(name = "date_modified")
  private LocalDate dateModified;

  @ManyToOne
  private Movie movie;
  @ManyToOne
  private Cinema cinema;
  @ManyToOne
  private User user;

  public Review(double rating, String reviewText, LocalDate dateModified, Movie movie, User user) {
    this.rating = rating;
    this.reviewText = reviewText;
    this.dateModified = dateModified;
    this.movie = movie;
    this.user = user;
  }

  public Review(double rating, String reviewText, LocalDate dateModified, Cinema cinema, User user) {
    this.rating = rating;
    this.reviewText = reviewText;
    this.dateModified = dateModified;
    this.cinema = cinema;
    this.user = user;
  }
}
