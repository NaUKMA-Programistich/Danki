package ua.ukma.edu.danki.utils

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import ua.ukma.edu.danki.models.*

object DatabaseFactory {

    fun init(driverClassName: String, jdbcUrl: String) {
        val database = Database.connect(jdbcUrl, driverClassName)

        transaction(database) {
            SchemaUtils.createMissingTablesAndColumns(Cards, CardCollections, Users, UserCardCollections, TermHistories)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}