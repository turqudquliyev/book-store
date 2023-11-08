package service

import az.ingress.model.request.CreateUserRequest
import az.ingress.model.request.RegisterationRequest
import az.ingress.service.abstraction.AuthorService
import az.ingress.service.abstraction.StudentService
import az.ingress.service.strategy.RegistrationStrategy
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

class RegistrationStrategyTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()
    StudentService studentService
    AuthorService authorService
    RegistrationStrategy registrationStrategy

    def setup() {
        studentService = Mock()
        authorService = Mock()
        registrationStrategy = new RegistrationStrategy(studentService, authorService)
    }

    def "TestRegisterUser success case"() {
        given:
        def request = random.nextObject(RegisterationRequest)
        def createUserRequest = CreateUserRequest.from(request)
        def role = request.role.name()

        when:
        registrationStrategy.register(request)

        then:
        if (role == "STUDENT") {
            1 * studentService.createStudent(createUserRequest)
        } else if (role == "AUTHOR") {
            1 * authorService.createAuthor(createUserRequest)
        }
    }
}