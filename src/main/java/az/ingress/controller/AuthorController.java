package az.ingress.controller;

import az.ingress.service.abstraction.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/v1/authors")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class AuthorController {
    AuthorService authorService;

    @PutMapping("/{id}/subscribe")
    @ResponseStatus(NO_CONTENT)
    public void subscribe(@PathVariable Long id, Long studentId) {
        authorService.subscribeAuthor(id, studentId);
    }
}