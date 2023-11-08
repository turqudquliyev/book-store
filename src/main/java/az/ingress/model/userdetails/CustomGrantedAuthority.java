package az.ingress.model.userdetails;

import az.ingress.dao.entity.RoleEntity;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class CustomGrantedAuthority implements GrantedAuthority {
    String authority;

    public CustomGrantedAuthority(RoleEntity authority) {
        this.authority = authority.getAuthority();
    }
}