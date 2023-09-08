package filmfocus.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@AllArgsConstructor
@Data
public class ProjectionDto {

  private int id;
  private double price;
  private HallDto hall;
  private ProgramDto program;
  private MovieDto movie;
  private LocalTime startTime;
}
