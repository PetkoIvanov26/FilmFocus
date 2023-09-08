package filmfocus.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequest {

  @NotNull(message = "The username can't be empty")
  private String username;

  @Pattern(regexp = "^.{8,}$", message = "The password should be at least 8 symbols long")
  @NotNull(message = "The password can't be empty")
  private String password;
}