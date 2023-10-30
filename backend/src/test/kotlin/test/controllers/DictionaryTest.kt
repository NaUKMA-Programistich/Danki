package test.controllers

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ua.ukma.edu.danki.models.dictionary.DictionarySuggestions
import ua.ukma.edu.danki.models.dictionary.GetDictionarySuggestions
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
}