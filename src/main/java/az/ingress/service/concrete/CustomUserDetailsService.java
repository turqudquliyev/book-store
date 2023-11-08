package az.ingress.service.concrete;

import az.ingress.aop.annotation.LogIgnore;
import az.ingress.dao.repository.UserRepository;
import az.ingress.model.userdetails.CustomGrantedAuthority;
import az.ingress.model.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class CustomUserDetailsService implements UserDetailsService {
    UserRepository userRepository;

    @LogIgnore
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username).orElseThrow();
        var customGrantedAuthorities = user.getAuthorities().stream().map(CustomGrantedAuthority::new).collect(Collectors.toSet());
        return new CustomUserDetails(user, customGrantedAuthorities);
    }
}