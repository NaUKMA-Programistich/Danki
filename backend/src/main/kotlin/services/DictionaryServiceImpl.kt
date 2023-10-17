package services

import dictionary.Dictionary
import ua.ukma.edu.danki.models.dictionary.FullTerm
import ua.ukma.edu.danki.models.dictionary.PartialTerm

class DictionaryServiceImpl(directoryPath: String) : DictionaryService {
    private val dictionary: Dictionary = Dictionary.fromDirectory(directoryPath)


    override fun getWordsFor(input: String): List<PartialTerm> {
        TODO("Not yet implemented")
    }

    override fun definitionFor(word: String): FullTerm {
        TODO("Not yet implemented")
    }

}