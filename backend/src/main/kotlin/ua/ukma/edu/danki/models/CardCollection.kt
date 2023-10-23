package ua.ukma.edu.danki.models

import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.*

object CardCollections : LongIdTable() {
    val name = varchar("name", length = 100)

    val lastModified = timestamp("last_modified").defaultExpression(CurrentTimestamp())
}

class CardCollection(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<CardCollection>(CardCollections)

    var name by CardCollections.name
    var lastModified by CardCollections.lastModified
}


object UserCardCollections : UUIDTable() {
    val collection = reference("card_collection", CardCollections, onDelete = ReferenceOption.CASCADE)
    val user = reference("user", Users, onDelete = ReferenceOption.CASCADE)
    val own = bool("own").default(true)
    val shared = bool("shared").default(false)
    val favorite = bool("favorite").default(false)
    val hidden = bool("hidden").default(false)
    val type = enumeration<CollectionType>("type").default(CollectionType.Normal)
}

enum class CollectionType {
    Normal,
    Recents
}

data class InternalCardCollectionDTO(
    val uuid: UUID,
    val user: UUID,
    val collection: Long,
    var name: String,
    var own: Boolean,
    var shared: Boolean,
    var favorite: Boolean,
    var lastModified: Instant
)

fun InternalCardCollectionDTO.toUserCardCollectionDTO(): UserCardCollectionDTO {
    return UserCardCollectionDTO(this.uuid.toString(), this.name, this.lastModified, this.own, this.favorite)
}

class UserCardCollection(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserCardCollection>(UserCardCollections)

    var collection by UserCardCollections.collection
    var user by UserCardCollections.user
    var own by UserCardCollections.own
    var shared by UserCardCollections.shared
    var favorite by UserCardCollections.favorite
    var hidden by UserCardCollections.hidden
    var type by UserCardCollections.type
}

