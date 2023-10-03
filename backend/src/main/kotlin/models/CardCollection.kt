package models

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.UUID

object CardCollections : UUIDTable() {
    val name = varchar("name", length = 100)
}

class CardCollection(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<CardCollection>(CardCollections)

    var name by CardCollections.name
}


fun chece() {

}