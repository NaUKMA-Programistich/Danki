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
import ua.ukma.edu.danki.models.UserRegisterRequest
import ua.ukma.edu.danki.models.UserRegisterResponse
import java.util.UUID

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

        val result = client.post("/register") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        val body = result.body<UserRegisterResponse>()

        Assertions.assertTrue(body.success)
    }
}