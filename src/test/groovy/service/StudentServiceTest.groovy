package service

import az.ingress.dao.entity.BookEntity
import az.ingress.dao.entity.StudentEntity
import az.ingress.dao.repository.StudentRepository
import az.ingress.exception.NotFoundException
import az.ingress.model.request.CreateUserRequest
import az.ingress.service.concrete.BookServiceHandler
import az.ingress.service.concrete.StudentServiceHandler
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import spock.lang.Specification

class StudentServiceTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()
    StudentRepository studentRepository
    BookServiceHandler bookServiceHandler
    StudentServiceHandler studentService

    def setup() {
        studentRepository = Mock()
        bookServiceHandler = Mock()
        studentService = new StudentServiceHandler(studentRepository, bookServiceHandler)
    }

    def "TestGetStudentById success case"() {
        given:
        def id = random.nextLong()
        def studentEntity = random.nextObject(StudentEntity)

        when:
        def actual = studentService.getStudentById(id)

        then:
        1 * studentRepository.findById(id) >> Optional.of(studentEntity)
        studentEntity == actual
    }

    def "TestGetStudentById StudentNotFound case"() {
        given:
        def id = random.nextLong()

        when:
        studentService.getStudentById(id)

        then:
        1 * studentRepository.findById(id) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.message == "STUDENT_NOT_FOUND"
    }

    def "TestCreateStudent success case"() {
        given:
        def createUserRequest = random.nextObject(CreateUserRequest)

        when:
        studentService.createStudent(createUserRequest)

        then:
        1 * studentRepository.save(_ as StudentEntity)
    }
}