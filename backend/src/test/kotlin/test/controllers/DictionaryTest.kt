package test.controllers

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
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

        val result = client.get(GetDictionarySuggestions("apple", 10))

        val text = result.bodyAsText()
        println(text)
        val body = result.body<DictionarySuggestions>()

        Assertions.assertTrue(body.suggestions.any { it.term == "apple" })


    }

    @Test
    fun testGetTermDefinition() = testApplication {
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

        val result = client.get(GetDictionarySuggestions("apple", 10))

        val text = result.bodyAsText()
        println(text)
        val body = result.body<DictionarySuggestions>()

        val definition = client.get(GetTermDefinition(body.suggestions[0].term, body.suggestions[0].references[0])) {
            headers {
                append("Accept", "application/json")
            }
        }
        println(definition.status)
        Assertions.assertTrue(definition.status == HttpStatusCode.OK)


        println(definition.bodyAsText())
        val definitionDecoded = definition.body<TermDefinition>()
        println(definitionDecoded)

        Assertions.assertTrue(body.suggestions.any { it.term == "apple" })

    }
}