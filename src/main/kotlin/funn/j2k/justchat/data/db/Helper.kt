package funn.j2k.justchat.data.db

import funn.j2k.justchat.data.model.MessageTable
import funn.j2k.justchat.data.model.UserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun initDatabase(database: Database) {
    transaction(database) {
        SchemaUtils.create(UserTable, MessageTable)
    }
}
