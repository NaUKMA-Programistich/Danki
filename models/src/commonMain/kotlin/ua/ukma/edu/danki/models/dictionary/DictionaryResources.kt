package ua.ukma.edu.danki.models.dictionary

import io.ktor.resources.*
import kotlinx.serialization.Serializable


@Resource("/dictionary/suggest/{input}")
class GetDictionarySuggestions(val input: String, val count: Int)

@Serializable
data class DictionarySuggestions(val suggestions: List<PartialTerm>)

@Resource("/dictionary/term/{term}")
data class GetTermDefinition(val term: String)

@Serializable
data class TermDefinition(val term: FullTerm)
