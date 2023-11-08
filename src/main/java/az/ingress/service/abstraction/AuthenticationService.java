package az.ingress.service.abstraction;

import az.ingress.model.jwt.RefreshTokenRequest;
import az.ingress.model.request.LoginRequest;
import az.ingress.model.response.LoginResponse;

public interface AuthenticationService {
    LoginResponse login(LoginRequest request);

    LoginResponse refresh(RefreshTokenRequest request);
}