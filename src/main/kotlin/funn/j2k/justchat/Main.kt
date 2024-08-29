package funn.j2k.justchat

import funn.j2k.justchat.event.Accept
import funn.j2k.justchat.event.Auth
import funn.j2k.justchat.event.Event
import funn.j2k.justchat.event.Message
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val selectorManager = SelectorManager(Dispatchers.IO)
    val serverSocket = aSocket(selectorManager).tcp().bind("127.0.0.1", 9002)

    while (true) {
        val socket = serverSocket.accept()

        launch {
            val receiveChannel = socket.openReadChannel()
            val sendChannel = socket.openWriteChannel(autoFlush = true)

            try {
                while (true) {
                    val event = Event.getEventReaderByCode(
                        receiveChannel.readByte().toInt()
                    ).readFullEvent(receiveChannel)
                    when (event) {
                        is Accept -> {

                        }
                        is Message -> {
                            println(event)
                            Accept(
                                Message.code.toByte(),
                                event.messageId,
                                ByteArray(1) { 42 /* new message id as payload */ }
                            ).send(sendChannel)
                        }

                        is Auth -> {
                            Accept(
                                Auth.code.toByte(),
                                69 /*user id*/,
                                "sdfsf".toByteArray() /*otp*/
                            ).send(sendChannel)
                        }
                    }
                }
            } catch (e: Throwable) {
                println(e.message)
                socket.close()
            }
        }
    }
}