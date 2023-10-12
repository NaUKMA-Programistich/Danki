package models

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import java.util.UUID

object CardCollections : LongIdTable() {
    val name = varchar("name", length = 100)

    val lastModified = datetime("last_modified").defaultExpression(CurrentDateTime)
}

class CardCollection(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<CardCollection>(CardCollections)

    var name by CardCollections.name
    var lastModified by CardCollections.lastModified
}


object UserCardCollections : UUIDTable() {

    val collection = reference("card_collection", CardCollections)
    val user = reference("user", Users)
    val own = bool("own").default(true)
    val shared = bool("shared").default(false)

}

class UserCardCollection(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserCardCollection>(UserCardCollections)

    var collection by UserCardCollections.collection
    var user by UserCardCollections.user
    var own by UserCardCollections.own
    var shared by UserCardCollections.shared

}

