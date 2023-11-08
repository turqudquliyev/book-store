package az.ingress.model.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Data
@FieldDefaults(level = PRIVATE)
public class AuthPayloadDto {
    String userId;
    String username;
}