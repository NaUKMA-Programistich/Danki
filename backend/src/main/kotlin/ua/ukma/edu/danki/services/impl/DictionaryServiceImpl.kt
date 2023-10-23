package ua.ukma.edu.danki.services.impl

import ua.ukma.edu.danki.dictionary.Dictionary
import ua.ukma.edu.danki.models.dictionary.FullTerm
import ua.ukma.edu.danki.models.dictionary.PartialTerm
import ua.ukma.edu.danki.services.DictionaryService
import java.util.*

class DictionaryServiceImpl(directoryPath: String) : DictionaryService {
    private val dictionary: Dictionary = Dictionary.fromDirectory(directoryPath)
    override fun getSuggestionsFor(input: String, count: Int): List<PartialTerm> {
        return dictionary.findTerms(input, count)
    }

    override fun definitionFor(term: PartialTerm): FullTerm {
        return dictionary.unwrapTerm(term)
    }

    override fun definitionForInput(input: String): Optional<FullTerm> {
        return dictionary.findFullTerm(input)
    }


}