package funn.j2k.justchat.domain.repository

import funn.j2k.justchat.domain.model.Message

interface MessageRepository {
    /**
     * create message and return actual message id
     * (the id field in message DTO may have any value, it does not affect to receipt id)
     * @return real message id after adding message to db
     */
    suspend fun addMessage(message: Message): Int
    suspend fun getMessageById(id: Int): Message
    suspend fun getMessagesById(startId: Int, limit: Int): List<Message>
}