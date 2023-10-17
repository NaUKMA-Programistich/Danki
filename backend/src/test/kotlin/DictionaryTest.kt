import dictionary.Dictionary
import java.nio.file.Path
import kotlin.test.Test

class DictionaryTest {
    @Test
    fun test() {
        val dir = Path.of("./src/main/resources/data")
        val dict = Dictionary.fromDirectory(dir.toString())

        val terms = dict.findTerms("app", 4)

        println(terms)
        val definition = dict.unwrapTerm(terms[0])

        println(definition)
    }
}