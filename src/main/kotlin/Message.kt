package funn.j2k

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*

interface EventReader {
    val code: Int // NOTE: Will be useful as annotation parameter with codegen?
    suspend fun readFullEvent(inputChannel: ByteReadChannel): Event
}

sealed interface Event {
    companion object {
        // TODO: Should i use codegen?
        fun getEventReaderByCode(code: Int): EventReader = when (code) {
            1 -> Accept
            2 -> Message
            else -> { error("Unknown event code") }
        }
    }

    suspend fun send(outputChannel: ByteWriteChannel)
}

data class Accept(val messageId: Int): Event {
    companion object : EventReader {
        override val code: Int = 1
        override suspend fun readFullEvent(inputChannel: ByteReadChannel): Accept {
            return Accept(inputChannel.readInt())
        }
    }

    override suspend fun send(outputChannel: ByteWriteChannel) = outputChannel.run {
        writeByte(code)
        writeInt(messageId)
    }
}

data class Message(val userId: Byte, val messageId: Int, val text: String): Event {
    companion object : EventReader {
        override val code: Int = 2

        override suspend fun readFullEvent(inputChannel: ByteReadChannel): Message {
            val userId = inputChannel.readByte()
            val messageId = inputChannel.readInt()
            val packetLength = inputChannel.readInt()
            val message = inputChannel.readPacket(packetLength).readText()

            return Message(userId, messageId, message)
        }
    }

    override suspend fun send(outputChannel: ByteWriteChannel) = outputChannel.run {
        outputChannel.writeByte(code)
        outputChannel.writeInt(messageId)
        val encodedText = text.encodeToByteArray()
        outputChannel.writeInt(encodedText.size)
        writePacket(ByteReadPacket(encodedText))
    }
}
