package filmfocus.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class ReviewDto {

  private int id;
  private double rating;
  private String reviewText;
  private LocalDate dateModified;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MovieDto movie;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private CinemaDto cinema;
  private UserDto user;
}
