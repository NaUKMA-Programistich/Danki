package test.controllers

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ua.ukma.edu.danki.models.*
import ua.ukma.edu.danki.models.auth.*
import java.util.*

class AuthControllerTests {
    @Test
    fun testValidRegistration() = testApplication {
        environment {
            config = ApplicationConfig("application-mock.yaml")
        }

        this.application {
            this.configureProductionModuleForTests()
        }


        val client = createClient {
            install(Resources)
            install(ContentNegotiation) {
                json()
            }
        }

        val request = UserRegisterRequest(
            username = "name",
            email = UUID.randomUUID().toString() + "@email.com",
            password = "pass"
        )

        val result = client.post(Register()) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        val body = result.body<UserRegisterResponse>()

        Assertions.assertTrue(body.success)
    }

    @Test
    fun testInvalidEmailRegistration() = testApplication {
        environment {
            config = ApplicationConfig("application-mock.yaml")
        }

        this.application {
            this.configureProductionModuleForTests()
        }


        val client = createClient {
            install(Resources)
            install(ContentNegotiation) {
                json()
            }
        }

        val request = UserRegisterRequest(
            username = "name",
            email = UUID.randomUUID().toString(),
            password = "pass"
        )

        val result = client.post(Register()) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        val body = result.body<ErrorMsg>()

        Assertions.assertTrue(body.message.isNotEmpty())
    }

    @Test
    fun testValidLogin() = testApplication {
        environment {
            config = ApplicationConfig("application-mock.yaml")
        }

        this.application {
            this.configureProductionModuleForTests()
        }


        val client = createClient {
            install(Resources)
            install(ContentNegotiation) {
                json()
            }
        }

        val requestToRegister = UserRegisterRequest(
            username = "name",
            email = UUID.randomUUID().toString() + "@email.com",
            password = "pass"
        )

        client.post(Register()) {
            contentType(ContentType.Application.Json)
            setBody(requestToRegister)
        }

        val requestToLogin = UserAuthRequest(email = requestToRegister.email, password = requestToRegister.password)

        val result = client.post(Login()) {
            contentType(ContentType.Application.Json)
            setBody(requestToLogin)
        }

        val body = result.body<UserAuthResponse>()

        Assertions.assertTrue(body.jwt.isNotEmpty())
    }

    @Test
    fun testUnsuccessfulLogin() = testApplication {
        environment {
            config = ApplicationConfig("application-mock.yaml")
        }

        this.application {
            this.configureProductionModuleForTests()
        }


        val client = createClient {
            install(Resources)
            install(ContentNegotiation) {
                json()
            }
        }

        val requestToRegister = UserRegisterRequest(
            username = "name",
            email = UUID.randomUUID().toString() + "@email.com",
            password = "pass"
        )

        client.post(Register()) {
            contentType(ContentType.Application.Json)
            setBody(requestToRegister)
        }

        val requestToLogin = UserAuthRequest(email = requestToRegister.email, password = "sfdsdf")

        val result = client.post(Login()) {
            contentType(ContentType.Application.Json)
            setBody(requestToLogin)
        }

        Assertions.assertEquals(HttpStatusCode.BadRequest, result.status)
    }
}