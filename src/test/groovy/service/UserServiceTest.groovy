package service

import az.ingress.dao.entity.UserEntity
import az.ingress.dao.repository.UserRepository
import az.ingress.exception.AlreadyExistsException
import az.ingress.exception.NotFoundException
import az.ingress.model.request.RegisterationRequest
import az.ingress.service.concrete.UserServiceHandler
import az.ingress.service.strategy.RegistrationStrategy
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

class UserServiceTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()
    PasswordEncoder passwordEncoder
    UserRepository userRepository
    RegistrationStrategy registrationStrategy
    UserServiceHandler userServiceHandler

    def setup() {
        passwordEncoder = Mock()
        userRepository = Mock()
        registrationStrategy = Mock()
        userServiceHandler = new UserServiceHandler(passwordEncoder, userRepository, registrationStrategy)
    }

    def "TestGetUserByEmail success case"() {
        given:
        def email = random.nextObject(String)
        def userEntity = random.nextObject(UserEntity)

        when:
        def actual = userServiceHandler.getUserByEmail(email)

        then:
        1 * userRepository.findByUsername(email) >> Optional.of(userEntity)
        userEntity.id == actual.id
        userEntity.username == actual.email
    }

    def "TestGetUserByEmail UserNotFound case"() {
        given:
        def email = random.nextObject(String)

        when:
        userServiceHandler.getUserByEmail(email)

        then:
        1 * userRepository.findByUsername(email) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.message == "USER_NOT_FOUND"
    }

    def "TestRegisterUser success case"() {
        given:
        def request = random.nextObject(RegisterationRequest)

        when:
        userServiceHandler.registerUser(request)

        then:
        1 * userRepository.existsByUsername(request.email) >> false
        1 * userRepository.save(_ as UserEntity)
        1 * registrationStrategy.register(request)
    }

    def "TestRegisterUser UserAlreadyExists case"() {
        given:
        def request = random.nextObject(RegisterationRequest)

        when:
        userServiceHandler.registerUser(request)

        then:
        1 * userRepository.existsByUsername(request.email) >> true
        AlreadyExistsException ex = thrown()
        ex.message == "USER_ALREADY_EXISTS"
    }

}
