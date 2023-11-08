package controller

import az.ingress.controller.UserController
import az.ingress.exception.GlobalExceptionHandler
import az.ingress.model.request.RegisterationRequest
import az.ingress.service.abstraction.UserService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static az.ingress.model.enums.UserRole.STUDENT
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

class UserControllerTest extends Specification {
    UserService userService
    UserController userController
    MockMvc mockMvc

    def setup() {
        userService = Mock()
        userController = new UserController(userService)
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build()
    }

    def "TestRegister success case"() {
        given:
        def url = "/v1/users"
        def registrationRequest = RegisterationRequest.builder()
                .email("email@email.com")
                .password("1234567890")
                .firstName("firstName")
                .lastName("lastname")
                .age(20)
                .role(STUDENT)
                .build()
        def jsonRequest =
                """
                    {
                      "firstName": "firstName",
                      "lastName": "lastname",
                      "age": 20,
                      "email": "email@email.com",
                      "password": "1234567890",
                      "role": "STUDENT"
                    }
                """

        when:
        mockMvc.perform(
                post(url)
                        .contentType(APPLICATION_JSON)
                        .content(jsonRequest)
        ).andReturn()
        then:
        1 * userService.registerUser(registrationRequest)
    }
}