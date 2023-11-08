package controller

import az.ingress.controller.BookController
import az.ingress.exception.GlobalExceptionHandler
import az.ingress.model.request.BookRequest
import az.ingress.model.response.BookResponse
import az.ingress.service.abstraction.BookService
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

class BookControllerTest extends Specification {
    BookService bookService
    BookController bookController
    MockMvc mockMvc

    def setup() {
        bookService = Mock()
        bookController = new BookController(bookService)
        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build()
    }

    def "TestGetById success case"() {
        given:
        def id = 1L
        def url = "/v1/books/$id"
        def bookResponse = BookResponse.builder()
                .id(id)
                .name("name")
                .isbn("isbn")
                .build()
        def expectedResponse =
                """
                    {
                      "id": 1,
                      "name": "name",
                      "isbn": "isbn"
                    }
                """
        when:
        def jsonResponse = mockMvc.perform(get(url)
                .contentType(APPLICATION_JSON)).andReturn()

        then:
        1 * bookService.getBookById(id) >> bookResponse
        jsonResponse.response.status == OK.value()
        JSONAssert.assertEquals(expectedResponse.toString(), jsonResponse.response.contentAsString.toString(), true)
    }

    def "TestCreate success case"() {
        given:
        def authorId = 2L
        def url = "/v1/books?authorId=$authorId"
        def bookRequest = BookRequest.builder()
                .name("name")
                .isbn("isbnisbnisbn")
                .build()
        def bookResponse = BookResponse.builder()
                .id(1L)
                .name("name")
                .isbn("isbnisbnisbn")
                .build()
        def jsonRequest =
                """
                    {
                      "name": "name",
                      "isbn": "isbnisbnisbn"
                    }
                """
        def expectedResponse =
                """
                    {
                      "id": 1,
                      "name": "name",
                      "isbn": "isbnisbnisbn"
                    }
                """

        when:
        def jsonResponse = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON)
                .content(jsonRequest)).andReturn()

        then:
        1 * bookService.createBook(authorId, bookRequest) >> bookResponse
        jsonResponse.response.status == CREATED.value()
        JSONAssert.assertEquals(expectedResponse.toString(), jsonResponse.response.contentAsString.toString(), true)
    }

    def "TestUpdateById success case"() {
        given:
        def id = 1L
        def authorId = 2L
        def url = "/v1/books/$id?authorId=$authorId"
        def bookRequest = BookRequest.builder()
                .name("name")
                .isbn("isbnisbnisbn")
                .build()
        def jsonRequest =
                """
                    {
                      "name": "name",
                      "isbn": "isbnisbnisbn"
                    }
                """

        when:
        mockMvc.perform(put(url)
                .contentType(APPLICATION_JSON)
                .content(jsonRequest)).andReturn()

        then:
        1 * bookService.updateBookById(authorId, id, bookRequest)
    }

    def "TestRead success case"() {
        given:
        def id = 1L
        def studentId = 2L
        def url = "/v1/books/$id/read?studentId=$studentId"

        when:
        mockMvc.perform(put(url)
                .contentType(APPLICATION_JSON)).andReturn()

        then:
        1 * bookService.readBook(id, studentId)
    }

    def "TestDeleteById success case"() {
        given:
        def id = 1L
        def authorId = 2L
        def url = "/v1/books/$id?authorId=$authorId"

        when:
        mockMvc.perform(delete(url)
                .contentType(APPLICATION_JSON)).andReturn()

        then:
        1 * bookService.deleteBookById(authorId, id)
    }
}