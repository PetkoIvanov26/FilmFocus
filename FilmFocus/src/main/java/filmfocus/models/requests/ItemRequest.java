package filmfocus.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemRequest {

  @NotNull(message = "The item name can't be empty")
  private String name;

  @Positive(message = "The price must be positive")
  @NotNull(message = "The price can't be empty")
  private double price;

  @Positive(message = "The quantity must be positive")
  @NotNull(message = "The quantity can't be empty")
  private int quantity;
}
