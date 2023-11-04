package ua.ukma.edu.danki.dto

import kotlinx.datetime.Instant
import ua.ukma.edu.danki.models.TermHistory
import java.util.UUID

data class TermHistoryDto(
    val id: Long,
    val user: UUID,
    val lastAccessed: Instant,
    val term: String
)


