package ua.ukma.edu.danki.models

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object Cards : LongIdTable() {
    val term = varchar("term", length = 50)
    val definition = text("definition")
    val collection = reference("card-collection", CardCollections).nullable()
}

class Card(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Card>(Cards)

    var term by Cards.term
    var definition by Cards.definition
    var collection by CardCollection optionalReferencedOn Cards.collection

}