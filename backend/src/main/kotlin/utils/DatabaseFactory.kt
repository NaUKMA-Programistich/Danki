package utils

import models.Card
import models.CardCollection
import models.CardCollections
import models.Cards
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init() {
        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:file:./build/db"
        val database = Database.connect(jdbcURL, driverClassName)

        transaction(database) {
            SchemaUtils.create(Cards)
            SchemaUtils.create(CardCollections)

            var hehe = Card.new {
                term = "hello"
                definition = "there"
            }

        }


        println("Database successful")
        transaction {
            //Card.all().forEach {
            //    println("${it.term} ${it.definition}")
            //}
        }
    }
}