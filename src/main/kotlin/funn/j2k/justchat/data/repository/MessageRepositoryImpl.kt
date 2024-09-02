package funn.j2k.justchat.data.repository

import funn.j2k.justchat.data.db.suspendTransaction
import funn.j2k.justchat.data.model.MessageDao
import funn.j2k.justchat.data.model.MessageTable
import funn.j2k.justchat.data.model.toMessage
import funn.j2k.justchat.domain.model.Message
import funn.j2k.justchat.domain.repository.MessageRepository
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insertAndGetId

class MessageRepositoryImpl(private val database: Database) : MessageRepository {
    override suspend fun addMessage(message: Message): Int = suspendTransaction(database) {
        MessageTable.insertAndGetId {
            it[authorId] = message.author.id
            it[text] = message.text
        }.value
    }

    override suspend fun getMessageById(id: Int): Message = suspendTransaction(database) {
        MessageDao[id].toMessage()
    }

    override suspend fun getMessagesById(startId: Int, limit: Int): List<Message> = suspendTransaction(database) {
        val query = MessageTable.select(MessageTable.columns)
            .where { MessageTable.id greaterEq startId }
            .limit(limit)

        MessageDao.wrapRows(query).map { it.toMessage() }
    }
}
