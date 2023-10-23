package ua.ukma.edu.danki.models.dictionary

import io.ktor.resources.*
import kotlinx.serialization.Serializable


@Resource("/dictionary/suggest/{input}?count={count}")
class GetDictionarySuggestions(val input: String, val count: Int)

@Serializable
data class DictionarySuggestions(val suggestions: List<PartialTerm>)

@Resource("/dictionary/term/{term}")
data class GetTermDefinition(val term: PartialTerm)

@Serializable
data class TermDefinition(val term: FullTerm)

@Resource("/dictionary/card-suggestion/{input}")
data class GetCardSuggestion(val input: String)

@Serializable
data class CardSuggestionResult(val term: FullTerm?)
