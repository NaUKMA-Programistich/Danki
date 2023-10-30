package ua.ukma.edu.danki.models

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

object TermHistories : LongIdTable() {
    val term = varchar("term", length = 100)
    val user = reference("username", Users.id)
    val dateAccessed = timestamp("lastAccessed").defaultExpression(CurrentTimestamp())

}

class TermHistory(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<TermHistory>(TermHistories)

    var term by TermHistories.term
    var user by TermHistories.user
    var dateAccessed by TermHistories.dateAccessed
}
