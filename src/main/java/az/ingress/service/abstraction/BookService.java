package az.ingress.service.abstraction;

import az.ingress.model.request.BookRequest;
import az.ingress.model.response.BookResponse;

public interface BookService {
    BookResponse getBookById(Long id);

    BookResponse createBook(Long authorId, BookRequest request);

    void updateBookById(Long authorId, Long id, BookRequest request);

    void deleteBookById(Long authorId, Long id);

    void readBook(Long id, Long studentId);
}