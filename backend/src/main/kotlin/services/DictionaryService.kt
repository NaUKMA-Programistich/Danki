package services

import ua.ukma.edu.danki.models.dictionary.FullTerm
import ua.ukma.edu.danki.models.dictionary.PartialTerm


interface DictionaryService {

    fun getWordsFor(input: String): List<PartialTerm>;

    fun definitionFor(word: String): FullTerm;
}
