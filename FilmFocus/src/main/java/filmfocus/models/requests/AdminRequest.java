package filmfocus.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminRequest {

  @NotNull(message = "The username can't be empty")
  private String username;

  @Pattern(regexp = "^.{8,}$", message = "The password should be at least 8 symbols long")
  @NotNull(message = "The password can't be empty")
  private String password;

  @Pattern(regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email pattern provided")
  @NotNull(message = "The email can't be empty")
  private String email;

  @NotNull(message = "The first name can't be empty")
  private String firstName;

  @NotNull(message = "The last name can't be empty")
  private String lastName;

  @NotNull(message = "The user role names can't be empty")
  private List<String> roleNames;
}