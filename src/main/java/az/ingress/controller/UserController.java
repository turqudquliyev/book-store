package az.ingress.controller;

import az.ingress.model.request.RegisterationRequest;
import az.ingress.service.abstraction.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class UserController {
    UserService userService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void register(@RequestBody @Valid RegisterationRequest request) {
        userService.registerUser(request);
    }
}