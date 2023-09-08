package filmfocus.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HallDto {

  private int id;
  private int capacity;
  private CinemaDto cinema;
}
