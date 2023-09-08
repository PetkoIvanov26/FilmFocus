package filmfocus.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class ProgramDto {

  private int id;
  private LocalDate programDate;
  private CinemaDto cinema;
}
