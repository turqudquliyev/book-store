package az.ingress.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class BookRequest {
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 255, message = "Name cannot be longer than 255 characters")
    String name;
    @NotBlank(message = "ISBN cannot be blank")
    @Size(min = 10, max = 13, message = "ISBN must be between 10 and 13 characters")
    String isbn;
}