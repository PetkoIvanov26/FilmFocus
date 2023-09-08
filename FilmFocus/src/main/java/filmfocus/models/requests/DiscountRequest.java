package filmfocus.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DiscountRequest {

  @Pattern(regexp = "^[A-Za-z0-9-_ ]*$",
           message = "The discount's type must contain only letters, digits, dashes, spaces and underscores")
  @NotNull(message = "The discount's type can't be empty")
  private String type;

  @Pattern(regexp = "^\\d{4}$", message = "The discount's code should be 4 symbols long")
  @NotNull(message = "The discount's code can't be empty")
  private String code;

  @DecimalMin(value = "1", message = "The discount's percentage can't be less than 1")
  @DecimalMax(value = "50", message = "The discount's percentage can't be more than 50")
  @NotNull(message = "The discount's percentage can't be empty")
  private int percentage;
}
