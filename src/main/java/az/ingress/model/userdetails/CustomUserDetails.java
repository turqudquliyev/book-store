package az.ingress.model.userdetails;

import az.ingress.dao.entity.UserEntity;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class CustomUserDetails implements UserDetails {
    String username;
    String password;
    boolean isAccountNonExpired;
    boolean isAccountNonLocked;
    boolean isCredentialsNonExpired;
    boolean isEnabled;
    Set<CustomGrantedAuthority> authorities;

    public CustomUserDetails(UserEntity user, Set<CustomGrantedAuthority> authorities) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.isAccountNonExpired = user.isAccountNonExpired();
        this.isAccountNonLocked = user.isAccountNonLocked();
        this.isCredentialsNonExpired = user.isCredentialsNonExpired();
        this.isEnabled = user.isEnabled();
        this.authorities = authorities;
    }
}