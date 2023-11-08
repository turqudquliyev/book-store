package controller

import az.ingress.controller.AuthorController
import az.ingress.exception.GlobalExceptionHandler
import az.ingress.service.abstraction.AuthorService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put

class AuthorControllerTest extends Specification {
    AuthorService authorService
    AuthorController authorController
    MockMvc mockMvc

    def setup() {
        authorService = Mock()
        authorController = new AuthorController(authorService)
        mockMvc = MockMvcBuilders.standaloneSetup(authorController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build()
    }

    def "TestSubscribe success case"() {
        given:
        def id = 1L
        def studentId = 2L
        def url = "/v1/authors/$id/subscribe?studentId=$studentId"

        when:
        mockMvc.perform(
                put(url)
                        .contentType(APPLICATION_JSON)
        ).andReturn()

        then:
        1 * authorService.subscribeAuthor(id, studentId)
    }
}