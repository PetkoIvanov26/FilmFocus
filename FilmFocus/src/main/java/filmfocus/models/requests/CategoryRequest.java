package filmfocus.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryRequest {

  @Pattern(regexp = "^[A-Z0-9][A-Z0-9a-z-, .]*$",
           message = "The category's name must start with a capital letter or a digit and must contain only letters, " +
                     "digits, dashes, commas, spaces and dots")
  @NotNull(message = "The category's name can't be empty")
  private String name;
}
