package filmfocus.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleRequest {

  @Pattern(regexp = "^[A-Z_]+$",
           message = "The role is allowed to contain only capital letters and underscores")
  @NotNull(message = "The role can't be empty")
  private String name;
}