package filmfocus.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CinemaDto {

  private int id;
  private String address;
  private String city;
  private double averageRating;
}
