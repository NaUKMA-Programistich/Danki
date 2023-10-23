package ua.ukma.edu.danki.services

import ua.ukma.edu.danki.models.dictionary.FullTerm
import ua.ukma.edu.danki.models.dictionary.PartialTerm
import java.util.*


interface DictionaryService {

    fun getSuggestionsFor(input: String, count: Int): List<PartialTerm>;

    fun definitionFor(term: PartialTerm): FullTerm;

    fun definitionForInput(input: String): Optional<FullTerm>
}
