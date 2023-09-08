package filmfocus.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectionRequest {

  @Positive(message = "The price must be positive")
  @NotNull(message = "The price can't be empty")
  private double price;

  @Positive(message = "The hall id must be positive")
  @NotNull(message = "The hall id can't be empty")
  private int hallId;

  @Positive(message = "The program id must be positive")
  @NotNull(message = "The program id can't be empty")
  private int programId;

  @Positive(message = "The movie id must be positive")
  @NotNull(message = "The movie id can't be empty")
  private int movieId;

  @NotNull(message = "The start time can't be empty")
  private LocalTime startTime;
}
