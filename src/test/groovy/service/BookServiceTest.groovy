package service

import az.ingress.dao.entity.AuthorEntity
import az.ingress.dao.entity.BookEntity
import az.ingress.dao.repository.BookRepository
import az.ingress.exception.NotFoundException
import az.ingress.model.request.BookRequest
import az.ingress.service.abstraction.BookService
import az.ingress.service.concrete.AuthorServiceHandler
import az.ingress.service.concrete.BookServiceHandler
import az.ingress.service.concrete.StudentServiceHandler
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import static az.ingress.model.enums.BookStatus.UPDATED

class BookServiceTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()
    BookRepository bookRepository
    AuthorServiceHandler authorServiceHandler
    StudentServiceHandler studentServiceHandler
    BookService bookService

    def setup() {
        bookRepository = Mock()
        authorServiceHandler = Mock()
        studentServiceHandler = Mock()
        bookService = new BookServiceHandler(bookRepository, authorServiceHandler, studentServiceHandler)
    }

    def "TestGetBookId success case"() {
        given:
        def bookId = random.nextLong()
        def bookEntity = random.nextObject(BookEntity)

        when:
        def actual = bookService.getBookById(bookId)

        then:
        1 * bookRepository.findById(bookId) >> Optional.of(bookEntity)
        bookEntity.id == actual.id
        bookEntity.name == actual.name
        bookEntity.isbn == actual.isbn
    }

    def "TestGetBookId BookNotFound case"() {
        given:
        def bookId = random.nextLong()

        when:
        bookService.getBookById(bookId)

        then:
        1 * bookRepository.findById(bookId) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.message == "BOOK_NOT_FOUND"
    }
}