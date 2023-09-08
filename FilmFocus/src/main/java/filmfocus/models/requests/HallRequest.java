package filmfocus.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HallRequest {

  @Min(value = 21, message = "The capacity must be at least 21")
  @Max(value = 100, message = "The capacity must be at most 100")
  @NotNull(message = "The capacity can't be empty")
  private int capacity;

  @Positive(message = "The cinema id must be positive")
  @NotNull(message = "The cinema id can't be empty")
  private int cinemaId;
}
