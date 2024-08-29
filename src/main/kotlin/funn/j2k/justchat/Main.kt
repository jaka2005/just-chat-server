package funn.j2k.justchat

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
                        is Accept -> {}
                        is Message -> {
                            println(event)
                            event.send(sendChannel)
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