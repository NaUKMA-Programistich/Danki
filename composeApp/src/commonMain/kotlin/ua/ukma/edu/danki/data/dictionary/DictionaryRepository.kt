package ua.ukma.edu.danki.data.dictionary

import ua.ukma.edu.danki.models.dictionary.DictionarySuggestions
import ua.ukma.edu.danki.models.dictionary.GetDictionarySuggestions
import ua.ukma.edu.danki.models.dictionary.GetTermDefinition
import ua.ukma.edu.danki.models.dictionary.TermDefinition

interface DictionaryRepository {
    suspend fun getDictionarySuggestion(request: GetDictionarySuggestions): DictionarySuggestions?

    suspend fun getTermDefinition(request: GetTermDefinition): TermDefinition?

}