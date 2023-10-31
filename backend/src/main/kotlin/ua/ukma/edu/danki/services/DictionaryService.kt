package ua.ukma.edu.danki.services

import ua.ukma.edu.danki.models.User
import ua.ukma.edu.danki.models.dictionary.FullTerm
import ua.ukma.edu.danki.models.dictionary.PartialTerm


interface DictionaryService {

    suspend fun getSuggestionsFor(input: String, count: Int): List<PartialTerm>

    suspend fun definitionFor(term: String, user: User?): FullTerm?


    suspend fun close()
}
