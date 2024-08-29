package funn.j2k.justchat.event

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*

interface EventReader {
    val code: Int // NOTE: Will be useful as annotation parameter with codegen?

    /**
     * Read full [Event] without event code(so their used for identify event) and return it
     */
    suspend fun readFullEvent(inputChannel: ByteReadChannel): Event
}

sealed interface Event {
    companion object {
        // TODO: Should i use codegen?
        fun getEventReaderByCode(code: Int): EventReader = when (code) {
            1 -> Accept
            2 -> Message
            3 -> Auth
            else -> { error("Unknown event code") }
        }
    }

    suspend fun send(outputChannel: ByteWriteChannel)
}


/**
 * Accept code like HTTP 200
 * @param id - is id of accepted Entity(it depends on context,
 * so if is [Message] accept is messageId, and in case with [Auth] accept is user id,
 * which you use for identify yourself)
 */
data class Accept(
    val eventCode: Byte,
    val id: Int,
    val payload: ByteArray = ByteArray(0)
): Event {
    companion object : EventReader {
        override val code: Int = 1
        override suspend fun readFullEvent(inputChannel: ByteReadChannel): Accept  = inputChannel.run {
            return Accept(
                readByte(),
                readInt(),
                readSizedPacket(),
            )
        }
    }

    override suspend fun send(outputChannel: ByteWriteChannel) = outputChannel.run {
        writeByte(code)
        writeByte(eventCode)
        writeInt(id)
        writeSizedPacket(payload)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Accept

        if (id != other.id) return false
        if (!payload.contentEquals(other.payload)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + payload.contentHashCode()
        return result
    }
}

data class Message(
    val userId: Byte,
    val messageId: Int,
    val text: String,
    val otp: String? = null
): Event {
    companion object : EventReader {
        override val code: Int = 2

        override suspend fun readFullEvent(inputChannel: ByteReadChannel): Message = inputChannel.run {
            val userId = readByte()
            val messageId = readInt() // TODO: I should generate messageId on server and send their with accept
            val message = readString()
            val otp = readString()

            return Message(userId, messageId, message, otp)
        }
    }

    override suspend fun send(outputChannel: ByteWriteChannel) = outputChannel.run {
        writeByte(code)
        writeByte(userId)
        writeInt(messageId)
        writeString(text)
    }
}

data class Auth(val username: String, val password: String): Event {
    companion object : EventReader {
        override val code: Int = 3

        override suspend fun readFullEvent(inputChannel: ByteReadChannel): Auth = inputChannel.run {
            val username = readString()
            val password = readString()

            Auth(username, password)
        }
    }

    override suspend fun send(outputChannel: ByteWriteChannel) { }
}

suspend fun ByteReadChannel.readSizedPacket(): ByteArray {
    val packetLength = readInt()
    return readPacket(packetLength).readBytes(packetLength)
}

suspend fun ByteWriteChannel.writeSizedPacket(packet: ByteArray) {
    writeInt(packet.size)
    writePacket(ByteReadPacket(packet))
}

suspend fun ByteReadChannel.readString(): String {
    val packetLength = readInt()
    return readPacket(packetLength).readText()
}

suspend fun ByteWriteChannel.writeString(string: String) {
    writeSizedPacket(string.encodeToByteArray())
}
