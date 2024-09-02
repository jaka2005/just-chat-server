package funn.j2k.justchat.data.model

import funn.j2k.justchat.domain.model.Message
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object MessageTable: IntIdTable("message") {
    val text = text("text")
    val authorId = reference("author_id", UserTable.id, onDelete = ReferenceOption.CASCADE)
}

class MessageDao(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<MessageDao>(MessageTable)
    var text by MessageTable.text
    var author by UserDao referencedOn MessageTable.authorId
}

fun MessageDao.toMessage() = Message(
    id = id.value,
    author = author.toUser(),
    text = text
)
