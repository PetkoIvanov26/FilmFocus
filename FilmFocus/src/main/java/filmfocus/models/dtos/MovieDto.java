package filmfocus.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

@AllArgsConstructor
@Data
public class MovieDto {

  private int id;
  private String title;
  private double averageRating;
  private String description;
  private LocalDate releaseDate;
  private Duration runtime;
  private CategoryDto category;
}
