package az.ingress.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Builder
@Data
@FieldDefaults(level = PRIVATE)
public class LoginResponse {
    String accessToken;
    String refreshToken;
}