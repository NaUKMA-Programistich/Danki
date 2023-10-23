package ua.ukma.edu.danki.utils

import kotlinx.coroutines.Dispatchers
import ua.ukma.edu.danki.models.CardCollections
import ua.ukma.edu.danki.models.Cards
import ua.ukma.edu.danki.models.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import ua.ukma.edu.danki.models.UserCardCollections

object DatabaseFactory {

    fun init(driverClassName: String, jdbcUrl: String) {
        val database = Database.connect(jdbcUrl, driverClassName)

        transaction(database) {
            SchemaUtils.createMissingTablesAndColumns(Cards, CardCollections, Users, UserCardCollections)
            //SchemaUtils.createMissingTablesAndColumns(Cards, CardCollections)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}