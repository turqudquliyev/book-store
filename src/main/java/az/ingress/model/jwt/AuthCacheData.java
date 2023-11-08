package az.ingress.model.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Data
@FieldDefaults(level = PRIVATE)
public class AuthCacheData implements Serializable {
    static final long serialVersionUID = 1L;
    AccessTokenClaimsSet accessTokenClaimsSet;
    String publicKey;
}