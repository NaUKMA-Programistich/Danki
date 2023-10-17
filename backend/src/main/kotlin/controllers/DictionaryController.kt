package controllers

import DICTIONARY_PATH
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.get
import services.DictionaryService
import services.DictionaryServiceImpl
import ua.ukma.edu.danki.models.dictionary.DictionarySuggestions
import ua.ukma.edu.danki.models.dictionary.GetDictionarySuggestions
import ua.ukma.edu.danki.models.dictionary.GetTermDefinition
import ua.ukma.edu.danki.models.dictionary.TermDefinition

fun Routing.dictionaryController() {
    val dictionaryServiceFactory: () -> DictionaryService = { DictionaryServiceImpl(DICTIONARY_PATH) }

    get<GetDictionarySuggestions> {
        val result = dictionaryServiceFactory().getWordsFor(it.input, it.count)
        call.respond(DictionarySuggestions(result))
    }

    get<GetTermDefinition> {
        val result = dictionaryServiceFactory().definitionFor(it.term)
        call.respond(TermDefinition(result))
    }
}
