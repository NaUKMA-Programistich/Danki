package ua.ukma.edu.danki.services.impl

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import ua.ukma.edu.danki.dto.TermHistoryDto
import ua.ukma.edu.danki.models.TermHistories
import ua.ukma.edu.danki.models.TermHistory
import ua.ukma.edu.danki.models.User
import ua.ukma.edu.danki.models.dictionary.FullTerm
import ua.ukma.edu.danki.services.RecentsService
import ua.ukma.edu.danki.utils.DatabaseFactory

class RecentsServiceImpl : RecentsService {


    override suspend fun addTermForUser(user: User, term: FullTerm) {
        DatabaseFactory.dbQuery {
            val previouslySearched = TermHistories
                .select(where = (TermHistories.user eq user.id) and (TermHistories.term eq term.term))
                .singleOrNull()
                ?.let { mapRowToTermHistory(it) }


            previouslySearched?.let {
                TermHistories.update(where = { TermHistories.id eq it.id }) {
                    it[TermHistories.dateAccessed]  = CurrentTimestamp()
                }
            }
                ?: TermHistory.new {
                    this.user = user.id
                    this.term = term.term
                }
        }
    }

    override suspend fun getHistory(user: User, offset: Int, limit: Int): List<TermHistoryDto> {
        return DatabaseFactory.dbQuery {
            return@dbQuery TermHistories
                .select(where = (TermHistories.user eq user.id))
                .orderBy(TermHistories.dateAccessed, order = SortOrder.DESC)
                .limit(limit, offset.toLong())
                .map(::mapRowToTermHistory)

        }
    }


    private fun mapRowToTermHistory(it: ResultRow): TermHistoryDto {
        return TermHistoryDto(
            it[TermHistories.id].value,
            it[TermHistories.user].value,
            it[TermHistories.dateAccessed],
            it[TermHistories.term],
        )
    }


}