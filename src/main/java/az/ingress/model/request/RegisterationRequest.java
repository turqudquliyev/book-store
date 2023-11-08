package az.ingress.model.request;

import az.ingress.model.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@FieldDefaults(level = PRIVATE)
public class RegisterationRequest {
    @NotBlank(message = "First name cannot be blank")
    @Size(max = 255, message = "First name cannot be longer than 255 characters")
    String firstName;
    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 255, message = "Last name cannot be longer than 255 characters")
    String lastName;
    @NotNull(message = "Age cannot be null")
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 150, message = "Age must be less than 150")
    Integer age;
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    String email;
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 255, message = "Password must be between 6 and 255 characters")
    String password;
    @NotNull(message = "Role cannot be null")
    UserRole role;
}