package dictionary

import io.ktor.utils.io.bits.*
import ua.ukma.edu.danki.models.dictionary.PartialTerm
import ua.ukma.edu.danki.models.dictionary.PartialUnwrappedData
import ua.ukma.edu.danki.models.dictionary.Reference
import ua.ukma.edu.danki.models.dictionary.WordType
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.RandomAccessFile
import java.util.Optional
import kotlin.io.path.Path

typealias Reader = RandomAccessReader


class WordTypeDictionary(
    val indexData: Reader,
    val indexReference: Reader,
    val indexWords: Reader,
    val relationData: Reader,
    val relationReference: Reader,
    val wordType: WordType

) {
    val indexSize = indexReference.length() / 16

    val relationSize = indexReference.length() / 8

    companion object {
        fun fromDirectory(path: String, wordType: WordType): WordTypeDictionary {
            fun openFile(name: String) = FileAccessReader(Path(path, name).toString())
            val indexData = openFile("indexData")
            val indexReference = openFile("indexReference")
            val indexWords = openFile("indexWords")
            val relationData = openFile("relationData")
            val relationReference = openFile("relationReference")

            return WordTypeDictionary(indexData, indexReference, indexWords, relationData, relationReference, wordType)

        }
    }

    fun nextTerm(): Optional<PartialTerm> {
        if (indexReference.position() >= indexReference.length()) {
            return Optional.empty()
        }
        val wordPosition = indexReference.readU64().toLong()
        val dataPosition = indexReference.readU64().toLong()

        indexWords.moveTo(wordPosition)

        val word = let {
            val wordLen = indexWords.readVariableU64().toInt()
            indexWords.readString(wordLen)
        }

        val dataLength = indexData.readVariableU64().toInt()

        val references = let {
            (0 until dataLength).map {
                Reference(indexData.readVariableU64(), wordType)
            }
        }

        return Optional.of(PartialTerm(word, references))
    }

    fun termAt(index: ULong): Optional<PartialTerm> {
        moveTo(index)
        return nextTerm()
    }

    fun moveTo(index: ULong) {
        val position = (index * 16UL).toLong()
        indexReference.moveTo(position)
    }

    fun partialDataAt(index: ULong): PartialUnwrappedData {
        val position = (index * 8UL).toLong()
        relationReference.moveTo(position)
        val referencePosition = relationReference.readU64().toLong()
        relationData.moveTo(referencePosition)
        val count = relationData.readVariableU64().toInt()
        val words = let {
            (0 until count).map {
                relationData.readVariableString()
            }
        }

        return PartialUnwrappedData(words, index)
    }


    fun wordAt(index: Long): Optional<String> {
        val position = (index * 16L)
        indexReference.moveTo(position)
        if (indexReference.length() <= indexReference.position()) {
            return Optional.empty()
        }

        val wordPosition = indexReference.readU64().toLong()
        indexReference.readU64()
        indexWords.moveTo(wordPosition)

        val word = indexWords.readVariableString()
        return Optional.of(word)
    }

    fun term(lowerBound: String): Optional<PartialTerm> {
        var min = 0L
        var max = indexSize - 1
        var next: String
        loop@ while (min <= max) {
            val mid = min + (max - min) / 2
            next = wordAt(mid).let {
                if (it.isPresent)
                    return@let it.get()
                return Optional.empty()
            }

            val comp = next.compareTo(lowerBound)
            when {
                comp < 0 -> min = mid + 1
                comp == 0 -> {
                    min = mid; break@loop;
                }

                comp > 0 && min != max -> {
                    max = mid - 1
                }

                comp > 1 -> break@loop
            }
        }

        return termAt(min.toULong())
    }


    //val wordType: WordType
    //val indexSize: ULong
    //val relationSize: ULong


}


interface RandomAccessReader {
    fun length(): Long

    @Throws(EofError::class)
    fun readByte(): Int

    fun moveTo(position: Long)

    fun position(): Long
}


class EofError : Error()


fun RandomAccessReader.readU64(): ULong {
    return (readByte().toULong() shl 56) or
            (readByte().toULong() shl 48) or
            (readByte().toULong() shl 40) or
            (readByte().toULong() shl 32) or
            (readByte().toULong() shl 24) or
            (readByte().toULong() shl 16) or
            (readByte().toULong() shl 8) or
            (readByte().toULong())
}

@Throws(EofError::class)
fun RandomAccessReader.readVariableU64(): ULong {
    var v = 0UL
    var shift = 0
    var next = readByte()


    while (next and 128 == 0) {

        v = v or (next.toULong() shl shift)
        next = readByte()
        shift += 7
    }

    v = v or ((next and 127).toULong() shl shift)
    return v
}

@Throws(EofError::class)
fun RandomAccessReader.readString(count: Int): String {
    val buffer = arrayListOf<Byte>()
    for (i in 0 until count) {
        val next = readByte()
        buffer.add(next.toByte())
        if (next and 128 != 0 && next and 32 == 0) {
            buffer.add(readByte().toByte())
        } else if (next and 128 != 0 && next and 16 == 0) {
            buffer.add(readByte().toByte())
            buffer.add(readByte().toByte())
        } else if (next and 128 != 0) {
            buffer.add(readByte().toByte())
            buffer.add(readByte().toByte())
            buffer.add(readByte().toByte())
        }
    }

    return String(buffer.toByteArray())
}

fun RandomAccessReader.readVariableString(): String {
    return readString(readVariableU64().toInt())
}


//class InMemoryReader(val byteArray: ByteArray) : RandomAccessReader {
//    override fun length(): Long {
//        return byteArray.size
//    }
//
//    override fun readU64(): ULong {
//        TODO("Not yet implemented")
//    }
//
//    override fun readByte(): ULong {
//        TODO("Not yet implemented")
//    }
//
//    override fun moveTo(position: Long) {
//        TODO("Not yet implemented")
//    }
//
//
//}

class FileAccessReader(path: String) : RandomAccessReader {

    val accessFile: RandomAccessFile
    var bufferedStream: BufferedInputStream

    init {
        val file = File(path)
        this.accessFile = RandomAccessFile(file, "r")
        this.bufferedStream = BufferedInputStream(FileInputStream(this.accessFile.fd))
    }

    override fun length(): Long {
        return accessFile.length()
    }

    fun close() {
        this.accessFile.close()
    }

    override fun readByte(): Int {
        val r = bufferedStream.read()
        if (r == -1) throw EofError()
        return r
    }

    override fun moveTo(position: Long) {
        this.accessFile.seek(position)
        this.bufferedStream = BufferedInputStream(FileInputStream(this.accessFile.fd))
    }

    override fun position(): Long {
        return this.accessFile.filePointer
    }
}