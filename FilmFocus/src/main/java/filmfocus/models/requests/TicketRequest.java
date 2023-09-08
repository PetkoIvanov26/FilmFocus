package filmfocus.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TicketRequest {

  @Positive(message = "The projection id must be positive")
  @NotNull(message = "The projection id can't be empty")
  private int projectionId;
}