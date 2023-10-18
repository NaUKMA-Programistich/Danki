package ua.ukma.edu.danki.dictionary

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import ua.ukma.edu.danki.models.dictionary.*
import java.nio.file.Path
import java.util.*

class Dictionary(
    private val noun: WordTypeDictionary,
    private val verb: WordTypeDictionary,
    private val adverb: WordTypeDictionary,
    private val adjective: WordTypeDictionary
) {


    companion object {
        fun fromDirectory(path: String): Dictionary {
            fun create(name: String, wordType: WordType) =
                WordTypeDictionary.fromDirectory(Path.of(path, name).toString(), wordType)

            return Dictionary(
                create("noun", WordType.Noun),
                create("verb", WordType.Verb),
                create("adverb", WordType.Adverb),
                create("adjective", WordType.Adjective)
            )
        }
    }

    fun getDictionary(wordType: WordType): WordTypeDictionary {
        return when (wordType) {
            WordType.Noun -> noun
            WordType.Verb -> verb
            WordType.Adjective -> adjective
            WordType.Satellite -> adjective
            WordType.Adverb -> adverb
        }
    }

    fun dataAt(wordType: WordType, index: ULong): UnwrappedData {
        val dict = getDictionary(wordType)
        dict.relationReference.moveTo((index * 8UL).toLong());
        val position = dict.relationReference.readU64()
        dict.relationData.moveTo(position.toLong())

        val words = with(dict) {
            val wordsCount = relationData.readVariableU64()
            (0 until wordsCount.toInt()).map {
                relationData.readString(relationData.readVariableU64().toInt())
            }
        }

        // Ignore for now (use cases)
        with(dict) {
            if (wordType == WordType.Verb) {
                val count = relationData.readVariableU64().toInt()
                (0 until count).map {
                    relationData.readVariableU64()
                    relationData.readVariableU64()
                }
            }
        }

        val definition = dict.relationData.readVariableString()

        val relations = with(dict) {
            val count = relationData.readVariableU64().toInt()
            val references = (0 until count).map {
                val relationIndex = relationData.readVariableU64()
                val wType = WordType.fromByte(relationData.readByte())
                val rType = RelationType.fromByte(relationData.readByte())
                Triple(relationIndex, wType, rType)
            }

            references.map { (relationIndex, wordType, relationType) ->
                Relation(
                    this@Dictionary.getDictionary(wordType).partialDataAt(relationIndex), relationType, wordType
                )
            }
        }

        return UnwrappedData(
            words, relations, definition, emptyList(), wordType
        )
    }

    fun dataAtReference(reference: Reference): UnwrappedData {
        return dataAt(reference.wordType, reference.index)
    }

    fun unwrapTerm(term: PartialTerm): FullTerm {
        val list = term.references.map {
            dataAtReference(it)
        }

        return FullTerm(term.term, list)
    }

    fun findTerms(lowerBound: String, count: Int): List<PartialTerm> {
        val higherBound = let {
            val lastChar = lowerBound.last().plus(1)
            lowerBound.slice(0 until lowerBound.length - 1) + lastChar
        }


        fun firstTerms(dict: WordTypeDictionary, lowerBound: String): Optional<PartialTerm> {
            return dict.term(lowerBound)
        }


        return runBlocking(
            Dispatchers.IO
        ) {


            val searchStart = listOf(
                noun, verb, adjective, adverb
            ).map {
                async(Dispatchers.IO) { firstTerms(it, lowerBound) }
            }.awaitAll().map {
                it.flatMap {
                    when {
                        it.term <= higherBound -> Optional.of(it)
                        else -> Optional.empty()
                    }
                }
            }

            val values = mutableListOf(
                searchStart[0] to noun, searchStart[1] to noun, searchStart[2] to noun, searchStart[3] to noun
            )


            val terms = arrayListOf<PartialTerm>()
            for (i in 0 until count) {
                var min = Optional.empty<Int>()
                for (j in values.indices) {
                    val item = values[j]
                    if (item.first.isPresent && (min.isEmpty || item.first.get().term < values[min.get()].first.get().term)) min =
                        Optional.of(j)
                }
                when {
                    min.isEmpty -> break
                    min.isPresent -> {
                        val v = values[min.get()]
                        var term = v.first.get()
                        values[min.get()] = v.second.nextTerm() to v.second

                        for (j in values.indices) {
                            if (j == min.get()) {
                                continue;
                            }

                            if (values[j].first.map { it.term == term.term }.orElse(false)) {
                                term = term.combine(values[j].first.get())
                                val nextTerm = values[j].first

                                values[j] = nextTerm to values[j].second
                            }
                        }
                        terms.add(term)
                    }
                }
            }
            return@runBlocking terms
        }

    }
}