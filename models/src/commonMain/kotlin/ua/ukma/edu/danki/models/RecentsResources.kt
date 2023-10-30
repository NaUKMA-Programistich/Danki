package ua.ukma.edu.danki.models

import io.ktor.resources.*
import kotlinx.serialization.Serializable


@Resource("/recents")
data class GetRecentTerms(
    val offset: Int = 0,
    val limit: Int = 0,
)

@Serializable
data class RecentTerm(val term: String);

@Serializable
data class RecentTerms(val terms: List<RecentTerm>)
