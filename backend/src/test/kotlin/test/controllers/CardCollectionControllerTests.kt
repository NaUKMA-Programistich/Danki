package test.controllers

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.datetime.Clock
import org.junit.jupiter.api.Test
import ua.ukma.edu.danki.models.CardCollectionDTO
import ua.ukma.edu.danki.models.User
import ua.ukma.edu.danki.models.UserAuthRequest
import ua.ukma.edu.danki.services.CardCollectionService
import ua.ukma.edu.danki.services.UserService
import ua.ukma.edu.danki.utils.JwtConfig
import java.util.*
import kotlin.test.assertEquals

class CardCollectionsControllerTests {

    @Test
    fun testGetCollections() = testApplication {
        // Create a mock service for CardCollectionService and UserService
        val cardCollectionService = mockk<CardCollectionService>()
        val userService = mockk<UserService>()

        // Mock responses for the service calls
        val userId = UUID.randomUUID()
        val user = mockk<User>()
        every { user.email } returns "email@email.com"
        every { user.password } returns "pass"
        coEvery { userService.findUser(userId) } returns user
        coEvery { userService.findUser("email@email.com") } returns user

        val collections = listOf(
            CardCollectionDTO(1, "Collection1", Clock.System.now()),
            CardCollectionDTO(2, "Collection2", Clock.System.now())
        )
        coEvery {
            cardCollectionService.getCollections(any(), any(), any(), any(), any())
        } returns collections
        environment {
            config = ApplicationConfig("test-profile.yaml")
        }

        application {
            mockModule(cardCollectionService, userService)
        }

        val client = createClient { }
        val jwt = JwtConfig.makeToken(UserAuthRequest(user.email, user.password))
        val response = client.get("/collections/") {
            url {
                parameters.append("userId", userId.toString())
            }
            headers {
                append("Authorization", "Bearer $jwt")
                append("Accept", "application/json")
            }
        }
        assertEquals(HttpStatusCode.OK, response.status)
    }
}
