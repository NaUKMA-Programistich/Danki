package ua.ukma.edu.danki.utils

import kotlinx.coroutines.Dispatchers
import ua.ukma.edu.danki.models.CardCollections
import ua.ukma.edu.danki.models.Cards
import ua.ukma.edu.danki.models.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init() {
        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:file:./build/db"
        val database = Database.connect(jdbcURL, driverClassName)

        transaction(database) {
            SchemaUtils.create(Cards)
            SchemaUtils.create(CardCollections)
            SchemaUtils.create(Users)

        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}