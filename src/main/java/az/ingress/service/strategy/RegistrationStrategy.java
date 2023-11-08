package az.ingress.service.strategy;

import az.ingress.model.request.CreateUserRequest;
import az.ingress.model.request.RegisterationRequest;
import az.ingress.service.abstraction.AuthorService;
import az.ingress.service.abstraction.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class RegistrationStrategy {
    StudentService studentService;
    AuthorService authorService;

    @Async
    public void register(RegisterationRequest request) {
        switch (request.getRole()) {
            case AUTHOR -> authorService.createAuthor(CreateUserRequest.from(request));
            case STUDENT -> studentService.createStudent(CreateUserRequest.from(request));
        }
    }
}