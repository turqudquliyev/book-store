package az.ingress.service.concrete;

import az.ingress.aop.annotation.Log;
import az.ingress.model.jwt.AuthPayloadDto;
import az.ingress.model.jwt.RefreshTokenRequest;
import az.ingress.model.request.LoginRequest;
import az.ingress.model.response.LoginResponse;
import az.ingress.service.abstraction.AuthenticationService;
import az.ingress.service.abstraction.TokenService;
import az.ingress.service.abstraction.UserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Log
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class AuthenticationServiceHandler implements AuthenticationService {
    AuthenticationManager authenticationManager;
    UserService userService;
    TokenService tokenService;

    public LoginResponse login(LoginRequest request) {
        authenticate(request);
        var user = userService.getUserByEmail(request.getEmail());
        return tokenService.generateToken(AuthPayloadDto.of(user.getId().toString(), user.getEmail()));
    }

    public LoginResponse refresh(RefreshTokenRequest request) {
        return tokenService.refreshToken(request.getRefreshToken());
    }

    private void authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
    }
}