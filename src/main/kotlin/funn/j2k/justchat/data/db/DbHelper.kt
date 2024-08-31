package funn.j2k.justchat.data.db

import funn.j2k.justchat.data.model.MessageTable
import funn.j2k.justchat.data.model.UserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DbHelper {
    val db by lazy {
        Database.connect("jdbc:sqlite:file:test?mode=memory&cache=shared", "org.sqlite.JDBC")
    }

    fun init() {
        transaction(db) {
            SchemaUtils.create(UserTable, MessageTable)
        }
    }
}
