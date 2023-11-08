package az.ingress.model.jwt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@FieldDefaults(level = PRIVATE)
@JsonInclude(NON_NULL)
public class AccessTokenClaimsSet implements Serializable {
    static final long serialVersionUID = 1L;
    String userId;
    String username;
    String iss;
    @JsonProperty("exp")
    Date expirationTime;
    @JsonProperty("iat")
    Date createdTime;
}