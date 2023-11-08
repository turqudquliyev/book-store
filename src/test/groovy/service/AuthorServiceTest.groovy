package service

import az.ingress.dao.entity.AuthorEntity
import az.ingress.dao.repository.AuthorRepository
import az.ingress.dao.repository.StudentRepository
import az.ingress.exception.NotFoundException
import az.ingress.model.request.CreateUserRequest
import az.ingress.service.abstraction.AuthorService
import az.ingress.service.concrete.AuthorServiceHandler
import az.ingress.service.concrete.StudentServiceHandler
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

class AuthorServiceTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()
    AuthorRepository authorRepository
    StudentServiceHandler studentServiceHandler
    StudentRepository studentRepository
    AuthorService authorService

    def setup() {
        studentRepository = Mock()
        authorRepository = Mock()
        studentServiceHandler = Mock()
        authorService = new AuthorServiceHandler(authorRepository, studentServiceHandler)
    }

    def "TestCreateAuthor success case"() {
        given:
        def createUserRequest = random.nextObject(CreateUserRequest)

        when:
        authorService.createAuthor(createUserRequest)

        then:
        1 * authorRepository.save(_ as AuthorEntity)
    }

    def "TestGetAuthorById success case"() {
        given:
        def authorId = random.nextLong()
        def authorEntity = random.nextObject(AuthorEntity)

        when:
        def actual = authorService.getAuthorById(authorId)

        then:
        1 * authorRepository.findById(authorId) >> Optional.of(authorEntity)
        authorEntity.id == authorId
        authorEntity.firstName == actual.firstName
        authorEntity.lastName == actual.lastName
        authorEntity.age == actual.age
        authorEntity.students == actual.students
        authorEntity.books == actual.books
    }

    def "TestGetAuthorById AuthorNotFound case"() {
        given:
        def authorId = random.nextLong()

        when:
        authorService.getAuthorById(authorId)

        then:
        1 * authorRepository.findById(authorId) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.message == "AUTHOR_NOT_FOUND"
    }
}