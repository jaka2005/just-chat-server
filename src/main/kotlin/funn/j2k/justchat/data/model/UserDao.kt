package funn.j2k.justchat.data.model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object UserTable: IntIdTable("user") {
    val username = varchar("username", 50)
    val password = varchar("password", 50)
}

class UserDao(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<UserDao>(UserTable)
    var username by UserTable.username
    var password by UserTable.password
}