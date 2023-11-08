package az.ingress.controller;

import az.ingress.model.request.BookRequest;
import az.ingress.model.response.BookResponse;
import az.ingress.service.abstraction.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/v1/books")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class BookController {
    BookService bookService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('AUTHOR','STUDENT')")
    public BookResponse getById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAnyAuthority('AUTHOR')")
    public BookResponse create(Long authorId, @RequestBody @Valid BookRequest request) {
        return bookService.createBook(authorId, request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('AUTHOR')")
    public void updateById(Long authorId,
                           @PathVariable Long id,
                           @RequestBody @Valid BookRequest request) {
        bookService.updateBookById(authorId, id, request);
    }

    @PutMapping("/{id}/read")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public void read(@PathVariable Long id, Long studentId) {
        bookService.readBook(id, studentId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('AUTHOR')")
    public void deleteById(Long authorId,
                           @PathVariable Long id) {
        bookService.deleteBookById(authorId, id);
    }
}