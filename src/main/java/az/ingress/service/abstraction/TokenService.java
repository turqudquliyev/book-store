package az.ingress.service.abstraction;

import az.ingress.model.jwt.AuthPayloadDto;
import az.ingress.model.response.LoginResponse;

public interface TokenService {
    LoginResponse generateToken(AuthPayloadDto dto);

    LoginResponse refreshToken(String refreshToken);

    AuthPayloadDto validateToken(String accessToken);
}
