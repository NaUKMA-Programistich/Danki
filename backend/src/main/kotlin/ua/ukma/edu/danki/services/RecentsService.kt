package ua.ukma.edu.danki.services

import ua.ukma.edu.danki.dto.TermHistoryDto
import ua.ukma.edu.danki.models.User
import ua.ukma.edu.danki.models.dictionary.FullTerm

interface RecentsService {

    suspend fun addTermForUser(user: User, term: FullTerm)

    suspend fun getHistory(user: User, offset: Int, limit: Int): List<TermHistoryDto>
}