package controller

import az.ingress.controller.AuthenticationController
import az.ingress.exception.GlobalExceptionHandler
import az.ingress.model.jwt.RefreshTokenRequest
import az.ingress.model.request.LoginRequest
import az.ingress.model.response.LoginResponse
import az.ingress.service.abstraction.AuthenticationService
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

class AuthenticationControllerTest extends Specification {
    AuthenticationService authenticationService
    AuthenticationController authenticationController
    MockMvc mockMvc

    def setup() {
        authenticationService = Mock()
        authenticationController = new AuthenticationController(authenticationService)
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build()
    }

    def "TestLogin success case"() {
        given:
        def url = "/v1/auth/login"
        def loginRequest = new LoginRequest("turqudquliyev@gmail.com", "@Turqud12!")
        def loginResponse = LoginResponse.of("accessToken", "refreshToken")
        def jsonRequest = """
                                    {
                                      "email": "turqudquliyev@gmail.com",
                                      "password": "@Turqud12!"
                                    }
                                """
        def expectedResponse = """
                                        {
                                          "accessToken": "accessToken",
                                          "refreshToken": "refreshToken"
                                        }
                                      """
        when:
        def jsonResponse = mockMvc.perform(
                post(url)
                        .contentType(APPLICATION_JSON)
                        .content(jsonRequest)
        ).andReturn()

        then:
        1 * authenticationService.login(loginRequest) >> loginResponse
        jsonResponse.response.status == OK.value()
        JSONAssert.assertEquals(expectedResponse.toString(), jsonResponse.response.contentAsString.toString(), true)
    }

    def "TestRefresh success case"() {
        given:
        def url = "/v1/auth/refresh"
        def refreshTokenRequest = new RefreshTokenRequest("refreshToken")
        def loginResponse = LoginResponse.of("accessToken", "refreshToken")
        def jsonRequest = """
                                    {
                                      "refreshToken": "refreshToken"
                                    }
                                """
        def expectedResponse = """
                                        {
                                          "accessToken": "accessToken",
                                          "refreshToken": "refreshToken"
                                        }
                                      """
        when:
        def jsonResponse = mockMvc.perform(
                post(url)
                        .contentType(APPLICATION_JSON)
                        .content(jsonRequest)
        ).andReturn()

        then:
        1 * authenticationService.refresh(refreshTokenRequest) >> loginResponse
        jsonResponse.response.status == OK.value()
        JSONAssert.assertEquals(expectedResponse.toString(), jsonResponse.response.contentAsString.toString(), true)
    }
}