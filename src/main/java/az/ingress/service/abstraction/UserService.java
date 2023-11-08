package az.ingress.service.abstraction;

import az.ingress.model.request.RegisterationRequest;
import az.ingress.model.response.UserResponse;

public interface UserService {
    UserResponse getUserByEmail(String email);

    void registerUser(RegisterationRequest request);
}