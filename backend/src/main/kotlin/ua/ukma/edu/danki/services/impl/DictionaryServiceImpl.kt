package ua.ukma.edu.danki.services.impl

import ua.ukma.edu.danki.dictionary.Dictionary
import ua.ukma.edu.danki.models.User
import ua.ukma.edu.danki.models.dictionary.FullTerm
import ua.ukma.edu.danki.models.dictionary.PartialTerm
import ua.ukma.edu.danki.services.DictionaryService
import ua.ukma.edu.danki.services.RecentsService

class DictionaryServiceImpl(
    directoryPath: String, private val recentsService: RecentsService
) : DictionaryService {
    private val dictionary: Dictionary = Dictionary.fromDirectory(directoryPath)
    override suspend fun getSuggestionsFor(input: String, count: Int): List<PartialTerm> {
        return dictionary.findTerms(input, count)
    }

    override suspend fun definitionFor(term: String, user: User?): FullTerm? {
        //dictionary.
        val fullTerm = dictionary.getSuggestion(term) ?: return null
        user?.let {
            recentsService.addTermForUser(it, fullTerm)
        }
        return fullTerm
    }

    override suspend fun close() {
        dictionary.close()
    }


}