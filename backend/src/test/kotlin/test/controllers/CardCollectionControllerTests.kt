package test.controllers

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.resources.serialization.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ua.ukma.edu.danki.models.*
import ua.ukma.edu.danki.services.CardCollectionService
import ua.ukma.edu.danki.services.UserService
import ua.ukma.edu.danki.services.impl.CardServiceImpl
import ua.ukma.edu.danki.services.impl.RecentsServiceImpl
import ua.ukma.edu.danki.utils.auth.JwtConfig
import java.util.*
import kotlin.test.assertEquals

class CardCollectionsControllerTests {

    @Test
    fun testGetCollectionsWithMocks() = testApplication {
        environment {
            config = ApplicationConfig("application-mock.yaml")
        }
        // Create a mock service for CardCollectionService and UserService
        val cardCollectionService = mockk<CardCollectionService>()
        val userService = mockk<UserService>()

        // Mock responses for the service calls
        val userId = UUID.randomUUID()
        val user = mockk<User>()
        every { user.id.value } returns userId
        every { user.email } returns "email@email.com"
        every { user.password } returns "pass"
        coEvery { userService.findUser(userId) } returns user
        coEvery { userService.findUser("email@email.com") } returns user
        // This should correspond to application-mock.yaml values
        JwtConfig.init("secret", "dankiTest", 3600000L, userService)
        var jwt = JwtConfig.makeToken(UserAuthRequest(user.email, user.password))
        coEvery { userService.authenticateUser(UserAuthRequest("email@email.com", "pass")) } returns jwt

        val collections = listOf(
            UserCardCollectionDTO(
                UUID.randomUUID().toString(), "Collection1", Clock.System.now(),
                own = false,
                favorite = false
            ),
            UserCardCollectionDTO(
                UUID.randomUUID().toString(), "Collection2", Clock.System.now(),
                own = false,
                favorite = false
            )
        )
        coEvery {
            cardCollectionService.getCollections(any(), any(), any(), any(), any(), any())
        } returns collections

        application {
            mockModule(
                cardCollectionService,
                userService,
                CardServiceImpl(userService, cardCollectionService),
                recentsService = RecentsServiceImpl()
            )
            jwt = JwtConfig.makeToken(UserAuthRequest(user.email, user.password))
        }

        val loginToken = Json.decodeFromString<UserAuthResponse>(client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(UserAuthRequest(user.email, user.password)))
        }.body<String>()).jwt

        val link: String = href(ResourcesFormat(), GetUserCollections())
        println(link)
        val response = client.get(link) {
            headers {
                append("Authorization", "Bearer $loginToken")
                append("Accept", "application/json")
            }
        }
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testRecentsCollectionCreation() = testApplication {
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

        client.post("/register") {
            contentType(ContentType.Application.Json)
            setBody(requestToRegister)
        }

        val requestToLogin = UserAuthRequest(email = requestToRegister.email, password = requestToRegister.password)

        val loginResult = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(requestToLogin)
        }

        val loginBody = loginResult.body<UserAuthResponse>()

        val result = client.get("/collections/recents") {
            io.ktor.http.headers { bearerAuth(loginBody.jwt) }
        }

        val body = result.body<UserCardCollectionDTO>()

        Assertions.assertEquals("Recent", body.name)
    }

    @Test
    fun testInvalidAuthToken() = testApplication {
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

        client.post("/register") {
            contentType(ContentType.Application.Json)
            setBody(requestToRegister)
        }

        val requestToLogin = UserAuthRequest(email = requestToRegister.email, password = requestToRegister.password)

        client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(requestToLogin)
        }


        val result = client.get("/collections/recents") {
            io.ktor.http.headers { bearerAuth(" sfadsf") }
        }

        Assertions.assertEquals(HttpStatusCode.Unauthorized, result.status)
    }

    @Test
    fun testCollectionCreation() = testApplication {
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

        client.post("/register") {
            contentType(ContentType.Application.Json)
            setBody(requestToRegister)
        }

        val requestToLogin = UserAuthRequest(email = requestToRegister.email, password = requestToRegister.password)

        val loginResult = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(requestToLogin)
        }

        val loginBody = loginResult.body<UserAuthResponse>()

        val createResult = client.post("/collections/") {
            contentType(ContentType.Application.Json)
            setBody(CreateCardCollectionRequest(name = "name"))
            io.ktor.http.headers { bearerAuth(loginBody.jwt) }
        }

        Assertions.assertEquals(HttpStatusCode.OK, createResult.status)

        val result = client.get(GetUserCollections()) {
            io.ktor.http.headers { bearerAuth(loginBody.jwt) }
        }

        val body = result.body<ListOfCollectionsResponse>()

        Assertions.assertEquals(1, body.cardCollections.size)
    }

    @Test
    fun testUpdateCollection() = testApplication {
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

        client.post("/register") {
            contentType(ContentType.Application.Json)
            setBody(requestToRegister)
        }

        val requestToLogin = UserAuthRequest(email = requestToRegister.email, password = requestToRegister.password)

        val loginResult = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(requestToLogin)
        }

        val loginBody = loginResult.body<UserAuthResponse>()

        val createResult = client.post("/collections/") {
            contentType(ContentType.Application.Json)
            setBody(CreateCardCollectionRequest(name = "name"))
            io.ktor.http.headers { bearerAuth(loginBody.jwt) }
        }

        Assertions.assertEquals(HttpStatusCode.OK, createResult.status)

        val createdCollection = createResult.body<CreateCardCollectionResponse>()

        val updateResult = client.put("/collections/") {
            contentType(ContentType.Application.Json)
            setBody(UpdateCollectionRequest(name = "newName", favorite = true, uuid = createdCollection.uuid))
            io.ktor.http.headers { bearerAuth(loginBody.jwt) }
        }

        Assertions.assertEquals(HttpStatusCode.OK, updateResult.status)
        Assertions.assertEquals(true, updateResult.body<GenericBooleanResponse>().success)

        val result = client.get(GetUserCollections()) {
            io.ktor.http.headers { bearerAuth(loginBody.jwt) }
        }

        val body = result.body<ListOfCollectionsResponse>()

        Assertions.assertEquals("newName", body.cardCollections[0].name)
    }

    @Test
    fun testCollectionDeletion() = testApplication {
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

        client.post("/register") {
            contentType(ContentType.Application.Json)
            setBody(requestToRegister)
        }

        val requestToLogin = UserAuthRequest(email = requestToRegister.email, password = requestToRegister.password)

        val loginResult = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(requestToLogin)
        }

        val loginBody = loginResult.body<UserAuthResponse>()

        val createResult = client.post("/collections/") {
            contentType(ContentType.Application.Json)
            setBody(CreateCardCollectionRequest(name = "name"))
            io.ktor.http.headers { bearerAuth(loginBody.jwt) }
        }

        Assertions.assertEquals(HttpStatusCode.OK, createResult.status)

        val deletionResult = client.post("/collections/delete") {
            contentType(ContentType.Application.Json)
            setBody(DeleteCollectionsRequest(listOf(createResult.body<CreateCardCollectionResponse>().uuid)))
            io.ktor.http.headers { bearerAuth(loginBody.jwt) }
        }

        Assertions.assertEquals(HttpStatusCode.OK, deletionResult.status)

        val result = client.get(GetUserCollections()) {
            io.ktor.http.headers { bearerAuth(loginBody.jwt) }
        }

        val body = result.body<ListOfCollectionsResponse>()

        Assertions.assertEquals(0, body.cardCollections.size)
    }

    @Test
    fun testSharingMechanism() = testApplication {
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

        val requestToRegister1 = UserRegisterRequest(
            username = "name",
            email = UUID.randomUUID().toString() + "@email.com",
            password = "pass"
        )

        val requestToRegister2 = UserRegisterRequest(
            username = "name",
            email = UUID.randomUUID().toString() + "@email.com",
            password = "pass"
        )

        client.post("/register") {
            contentType(ContentType.Application.Json)
            setBody(requestToRegister1)
        }

        client.post("/register") {
            contentType(ContentType.Application.Json)
            setBody(requestToRegister2)
        }

        val requestToLogin1 = UserAuthRequest(email = requestToRegister1.email, password = requestToRegister1.password)
        val requestToLogin2 = UserAuthRequest(email = requestToRegister2.email, password = requestToRegister2.password)

        val loginResult1 = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(requestToLogin1)
        }

        val loginResult2 = client.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(requestToLogin2)
        }

        val loginBody1 = loginResult1.body<UserAuthResponse>()
        val loginBody2 = loginResult2.body<UserAuthResponse>()

        val createResult = client.post("/collections/") {
            contentType(ContentType.Application.Json)
            setBody(CreateCardCollectionRequest(name = "name"))
            io.ktor.http.headers { bearerAuth(loginBody1.jwt) }
        }

        Assertions.assertEquals(HttpStatusCode.OK, createResult.status)

        val shareResult = client.post("/collections/share") {
            contentType(ContentType.Application.Json)
            setBody(ShareCollectionRequest(uuid = createResult.body<CreateCardCollectionResponse>().uuid))
            io.ktor.http.headers { bearerAuth(loginBody1.jwt) }
        }

        Assertions.assertEquals(HttpStatusCode.OK, shareResult.status)

        val getSharedResult = client.post("/collections/get-shared") {
            contentType(ContentType.Application.Json)
            setBody(ReadSharedCollectionRequest(shareResult.body<ShareCollectionResponse>().id))
            io.ktor.http.headers { bearerAuth(loginBody2.jwt) }
        }

        Assertions.assertEquals(HttpStatusCode.OK, getSharedResult.status)

        val updateCollectionResponse = client.put("/collections/") {
            contentType(ContentType.Application.Json)
            setBody(
                UpdateCollectionRequest(
                    name = "newName",
                    favorite = true,
                    uuid = createResult.body<CreateCardCollectionResponse>().uuid
                )
            )
            io.ktor.http.headers { bearerAuth(loginBody1.jwt) }
        }

        val result = client.get(GetUserCollections()) {
            io.ktor.http.headers { bearerAuth(loginBody2.jwt) }
        }

        val body = result.body<ListOfCollectionsResponse>()

        Assertions.assertEquals(HttpStatusCode.OK, updateCollectionResponse.status)

        Assertions.assertEquals(1, body.cardCollections.size)
        Assertions.assertEquals("newName", body.cardCollections[0].name)
        Assertions.assertEquals(false, body.cardCollections[0].favorite)
    }
}
