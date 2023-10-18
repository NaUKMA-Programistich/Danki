package ua.ukma.edu.danki.models.dictionary

import kotlinx.serialization.Serializable

@Serializable
data class Relation(
    val relation: PartialUnwrappedData,
    val relationType: RelationType,
    val wordType: WordType
)


@Serializable
data class PartialUnwrappedData(
    val words: List<String>,
    val index: ULong
)

@Serializable
data class UnwrappedData(
    val words: List<String>,
    val relations: List<Relation>,
    val definition: String,
    val useCases: List<UseCases>,
    val wordType: WordType
)


@Serializable
data class UseCases(
    val id: ULong,
    val wordIndex: ULong
)

@Serializable
data class FullTerm(val term: String, val data: List<UnwrappedData>)

@Serializable
data class Reference(
    val index: ULong,
    val wordType: WordType,
)

@Serializable
data class PartialTerm(val term: String, val references: List<Reference>) {
    fun combine(other: PartialTerm): PartialTerm {
        return PartialTerm(term, references + other.references)
    }
}

@Serializable
enum class RelationType {
    Antonym, MemberHolonym, PartHolonym, SubstanceHolonym, VerbGroup, MemberMeronym, PartMeronym, SubstanceMeronym, SimilarTo, Entailment, DerivationallyRelatedForm, MemberOfThisDomainTopic, MemberOfThisDomainRegion, MemberOfThisDomainUsage, DomainOfSynsetTopic, DomainOfSynsetRegion, DomainOfSynsetUsage, ParticipleOfVerb, Attribute, Cause, Hypernym, InstanceHypernym, DerivedFromAdjective, PertainsToNoun, AlsoSee, Hyponym, InstanceHyponym;


    companion object {
        fun fromByte(byte: Int): RelationType {
            return when (byte) {
                0 -> Antonym
                1 -> MemberHolonym
                2 -> PartHolonym
                3 -> SubstanceHolonym
                4 -> VerbGroup
                5 -> MemberMeronym
                6 -> PartMeronym
                7 -> SubstanceMeronym
                8 -> SimilarTo
                9 -> Entailment
                10 -> DerivationallyRelatedForm
                11 -> MemberOfThisDomainTopic
                12 -> MemberOfThisDomainRegion
                13 -> MemberOfThisDomainUsage
                14 -> DomainOfSynsetTopic
                15 -> DomainOfSynsetRegion
                16 -> DomainOfSynsetUsage
                17 -> ParticipleOfVerb
                18 -> Attribute
                19 -> Cause
                20 -> Hypernym
                21 -> InstanceHypernym
                22 -> DerivedFromAdjective
                23 -> PertainsToNoun
                24 -> AlsoSee
                25 -> Hyponym
                26 -> InstanceHyponym
                else -> throw IllegalArgumentException("Byte can't be $byte")
            }
        }
    }
}

@Serializable
enum class WordType {
    Noun, Verb, Adjective, Satellite, Adverb;

    fun fromChar(value: Char): WordType {
        return when (value) {
            's' -> Satellite
            'r' -> Adverb
            'n' -> Noun
            'v' -> Verb
            'a' -> Adjective
            else -> throw IllegalArgumentException("Char can't be converted to WordType: $value")
        }
    }

    companion object {
        fun fromByte(readByte: Int): WordType {
            return when (readByte) {
                0 -> Noun
                1 -> Verb
                2 -> Adjective
                3 -> Satellite
                4 -> Adverb
                else -> throw IllegalArgumentException("Byte can't be $readByte")
            }
        }
    }
}
