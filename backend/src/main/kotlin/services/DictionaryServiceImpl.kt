package services

import dictionary.Dictionary
import ua.ukma.edu.danki.models.dictionary.FullTerm
import ua.ukma.edu.danki.models.dictionary.PartialTerm

class DictionaryServiceImpl(directoryPath: String) : DictionaryService {
    private val dictionary: Dictionary = Dictionary.fromDirectory(directoryPath)
    override fun getSuggestionsFor(input: String, count: Int): List<PartialTerm> {
        return dictionary.findTerms(input, count)
    }

    override fun definitionFor(term: PartialTerm): FullTerm {
        return dictionary.unwrapTerm(term)
    }


}