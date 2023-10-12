package services

import ua.ukma.edu.danki.models.TermDefinitions

interface DictionaryService {

    fun getWordsFor(input: String): List<String>;

    fun definitionFor(word: String): TermDefinitions;
}
