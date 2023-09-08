package filmfocus.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProgramRequest {

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @NotNull(message = "The program date can't be empty")
  private LocalDate programDate;

  @Positive(message = "The cinema id must be positive")
  private int cinemaId;
}
