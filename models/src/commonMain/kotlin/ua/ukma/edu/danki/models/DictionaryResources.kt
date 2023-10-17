package ua.ukma.edu.danki.models

import io.ktor.resources.*


@Resource("/dictionary/suggest/{input}?count={count}")
class GetDictionarySuggestions(val input: String, val count: Int)
data class DictionarySuggestions(val suggestions: List<String>)

@Resource("/dictionary/term/{term}")
data class GetTermDefinition(val term: String)
