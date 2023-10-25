package ua.ukma.edu.danki.models

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import ua.ukma.edu.danki.models.CardCollections.defaultExpression

object Cards : LongIdTable() {
    val term = varchar("term", length = 50)
    val definition = text("definition")
    val collection = reference("card-collection", CardCollections).nullable()
    val lastModified = timestamp("last_modified").defaultExpression(CurrentTimestamp())
    val timeAdded = timestamp("time_added").defaultExpression(CurrentTimestamp())
}

class Card(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Card>(Cards)

    var term by Cards.term
    var definition by Cards.definition
    var collection by CardCollection optionalReferencedOn Cards.collection
    val timeAdded by Cards.timeAdded
    var lastModified by Cards.lastModified
}