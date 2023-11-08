package az.ingress.service.concrete;

import az.ingress.aop.annotation.Log;
import az.ingress.dao.entity.AuthorEntity;
import az.ingress.dao.entity.BookEntity;
import az.ingress.dao.entity.StudentEntity;
import az.ingress.dao.repository.BookRepository;
import az.ingress.exception.NotFoundException;
import az.ingress.model.request.BookRequest;
import az.ingress.model.response.BookResponse;
import az.ingress.service.abstraction.BookService;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static az.ingress.mapper.BookMapper.BOOK_MAPPER;
import static az.ingress.model.constant.ExceptionConstant.BOOK_NOT_FOUND;
import static az.ingress.model.enums.BookStatus.DELETED;
import static az.ingress.model.enums.BookStatus.UPDATED;
import static lombok.AccessLevel.PRIVATE;

@Log
@Service
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class BookServiceHandler implements BookService {
    BookRepository bookRepository;
    AuthorServiceHandler authorService;
    StudentServiceHandler studentService;

    public BookServiceHandler(BookRepository bookRepository,
                              @Lazy AuthorServiceHandler authorService,
                              @Lazy StudentServiceHandler studentService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.studentService = studentService;
    }

    public BookResponse getBookById(Long id) {
        var book = fetchIfExist(id);
        return BOOK_MAPPER.mapEntityToResponse(book);
    }

    @SneakyThrows
    @Transactional
    public BookResponse createBook(Long authorId,
                                   BookRequest request) {
        var author = authorService.getAuthorById(authorId);
        var book = BOOK_MAPPER.mapRequestToEntity(request);
        setEntityRelations(author, book);
        var saved = bookRepository.save(book);
        return BOOK_MAPPER.mapEntityToResponse(saved);
    }

    public void updateBookById(Long authorId,
                               Long id,
                               BookRequest request) {
        var author = authorService.getAuthorById(id);
        var book = bookRepository.findByIdAndAuthor(id, author);
        book.ifPresent(b -> update(b, request));
    }

    public void deleteBookById(Long authorId, Long id) {
        var author = authorService.getAuthorById(id);
        var book = bookRepository.findByIdAndAuthor(id, author);
        book.ifPresent(this::setDeleteStatus);
    }

    @Transactional
    public void readBook(Long id, Long studentId) {
        var book = fetchIfExist(id);
        var student = studentService.getStudentById(id);
        setEntityRelations(book, student);
    }

    private void setEntityRelations(BookEntity book, StudentEntity student) {
        book.setStudents(Set.of(student));
        student.setBooks(Set.of(book));
    }

    private void setDeleteStatus(BookEntity book) {
        book.setStatus(DELETED);
        bookRepository.save(book);
    }

    private void update(BookEntity book, BookRequest request) {
        book.setName(request.getName());
        book.setIsbn(request.getIsbn());
        book.setStatus(UPDATED);
        bookRepository.save(book);
    }

    public BookEntity fetchIfExist(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(BOOK_NOT_FOUND));
    }

    private void setEntityRelations(AuthorEntity author, BookEntity book) {
        book.setAuthor(author);
        author.setBooks(Set.of(book));
    }
}