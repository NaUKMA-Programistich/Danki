package test.controllers

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ua.ukma.edu.danki.models.auth.*
import ua.ukma.edu.danki.models.dictionary.DictionarySuggestions
import ua.ukma.edu.danki.models.dictionary.GetDictionarySuggestions
import ua.ukma.edu.danki.models.dictionary.GetTermDefinition
import ua.ukma.edu.danki.models.dictionary.TermDefinition
import ua.ukma.edu.danki.module
import ua.ukma.edu.danki.services.CardCollectionService
import ua.ukma.edu.danki.services.UserService
import java.util.*

class DictionaryTest {


    @Test
    fun testGetTerm() = testApplication {
        environment {
            config = ApplicationConfig("application-mock.yaml")
        }

        this.application {
            module()
        }


        val client = createClient {
            install(Resources)
            install(ContentNegotiation) {
                json()
            }
        }

        val appleResponse = client.get(GetDictionarySuggestions("apple", 10))

        val body = appleResponse.body<DictionarySuggestions>()

        val apple = body.suggestions.find { it.term == "apple" }

        Assertions.assertTrue(apple != null)

        apple!!


        val definitionResponse = client.get(GetTermDefinition(apple.term))

        Assertions.assertEquals(definitionResponse.status, HttpStatusCode.OK)
        val definition = definitionResponse.body<TermDefinition>()
        Assertions.assertEquals(definition.term.term, "apple")
        println(definition)


    }

    @Test
    fun testGetCollectionsWithMocks() = testApplication {
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

        val authClient = createClient {
            install(Resources)
            install(ContentNegotiation) {
                json()
            }
            //install(Auth)
        }
        val appleResponse = client

            .get(GetDictionarySuggestions("apple", 10))

        val suggestions = appleResponse.body<DictionarySuggestions>().suggestions

        val apple = suggestions.find { it.term == "apple" }

        Assertions.assertTrue(apple != null)

        apple!!


        val definitionResponse = client.get(GetTermDefinition(apple.term))

        Assertions.assertEquals(definitionResponse.status, HttpStatusCode.OK)
        val definition = definitionResponse.body<TermDefinition>()
        Assertions.assertEquals(definition.term.term, "apple")
        println(definition)


    }

}