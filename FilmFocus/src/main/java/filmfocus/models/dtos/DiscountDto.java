package filmfocus.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DiscountDto {

  private int id;
  private String type;
  private String code;
  private int percentage;
}
