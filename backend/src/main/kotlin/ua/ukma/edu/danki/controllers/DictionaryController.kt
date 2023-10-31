package ua.ukma.edu.danki.controllers

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ua.ukma.edu.danki.models.dictionary.*
import ua.ukma.edu.danki.services.DictionaryService
import ua.ukma.edu.danki.services.UserService
import ua.ukma.edu.danki.utils.auth.extractOptionalUserFromJWT

fun Routing.dictionaryController(
    dictionaryServiceFactory: () -> DictionaryService,
    userService: UserService
) {

    authenticate("auth-jwt", optional = true) {

        get<GetDictionarySuggestions> {
            val dictionary = dictionaryServiceFactory()
            val result = dictionary.getSuggestionsFor(it.input, it.count)
            call.respond(DictionarySuggestions(result))
            dictionary.close()
        }

        get<GetTermDefinition> {
            val user = extractOptionalUserFromJWT(userService)
            val dictionary = dictionaryServiceFactory()
            val result = dictionary.definitionFor(PartialTerm(it.term, listOf(it.references)), user)
            call.respond(TermDefinition(result))
            dictionary.close()
        }
    }
}


