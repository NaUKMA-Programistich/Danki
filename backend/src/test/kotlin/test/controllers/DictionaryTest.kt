package test.controllers

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ua.ukma.edu.danki.models.dictionary.DictionarySuggestions
import ua.ukma.edu.danki.models.dictionary.GetDictionarySuggestions
import ua.ukma.edu.danki.models.dictionary.GetTermDefinition
import ua.ukma.edu.danki.models.dictionary.TermDefinition
import ua.ukma.edu.danki.module

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
}