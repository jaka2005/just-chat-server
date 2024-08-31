package funn.j2k.justchat.domain.model

data class Message(
    val id: Int,
    val author: User,
    val text: String
)
