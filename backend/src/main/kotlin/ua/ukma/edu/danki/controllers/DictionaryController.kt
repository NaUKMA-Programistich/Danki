package ua.ukma.edu.danki.controllers

import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ua.ukma.edu.danki.DICTIONARY_PATH
import ua.ukma.edu.danki.models.dictionary.*
import ua.ukma.edu.danki.services.impl.DictionaryServiceImpl
import ua.ukma.edu.danki.services.DictionaryService
import kotlin.jvm.optionals.getOrNull

fun Routing.dictionaryController() {
    val dictionaryServiceFactory: () -> DictionaryService = { DictionaryServiceImpl(DICTIONARY_PATH) }

    get<GetDictionarySuggestions> {
        val result = dictionaryServiceFactory().getSuggestionsFor(it.input, it.count)
        call.respond(DictionarySuggestions(result))
    }

    get<GetTermDefinition> {
        val result = dictionaryServiceFactory().definitionFor(it.term)
        call.respond(TermDefinition(result))
    }

    get<GetCardSuggestion> {
        val result = dictionaryServiceFactory().definitionForInput(it.input)
        call.respond(CardSuggestionResult(result.getOrNull()))
    }
}


