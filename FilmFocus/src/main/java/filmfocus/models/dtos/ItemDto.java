package filmfocus.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemDto {

  private int id;
  private String name;
  private Double price;
  private int quantity;
}
