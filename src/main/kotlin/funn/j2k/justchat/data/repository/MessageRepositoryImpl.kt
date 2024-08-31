package funn.j2k.justchat.data.repository

import funn.j2k.justchat.domain.model.Message
import funn.j2k.justchat.domain.repository.MessageRepository
import org.jetbrains.exposed.sql.Database

class MessageRepositoryImpl(private val database: Database) : MessageRepository {
    override suspend fun addMessage(message: Message): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getMessageById(id: Int): Message {
        TODO("Not yet implemented")
    }

    override suspend fun getMessagesById(startId: Int, limit: Int): List<Message> {
        TODO("Not yet implemented")
    }
}