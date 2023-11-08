package az.ingress.filter;

import az.ingress.service.abstraction.TokenService;
import az.ingress.service.concrete.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

import static az.ingress.model.constant.HeaderConstant.AUTH_HEADER;
import static az.ingress.model.constant.HeaderConstant.BEARER_AUTH_HEADER;
import static lombok.AccessLevel.PRIVATE;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class JwtAuthFilter extends OncePerRequestFilter {
    TokenService tokenService;
    CustomUserDetailsService userDetailsService;

    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().contains("/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader(AUTH_HEADER);
        final String jwt;
        if (authHeader == null || !authHeader.startsWith(BEARER_AUTH_HEADER)) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        var auth = tokenService.validateToken(jwt);
        if (auth.getUsername() != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var user = userDetailsService.loadUserByUsername(auth.getUsername());
            if (Objects.equals(auth.getUsername(), user.getUsername())) {
                var authToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}