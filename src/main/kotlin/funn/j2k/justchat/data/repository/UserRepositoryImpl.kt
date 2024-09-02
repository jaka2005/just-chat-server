package funn.j2k.justchat.data.repository

import funn.j2k.justchat.data.db.suspendTransaction
import funn.j2k.justchat.data.model.UserDao
import funn.j2k.justchat.data.model.toUser
import funn.j2k.justchat.domain.model.User
import funn.j2k.justchat.domain.repository.UserRepository
import org.jetbrains.exposed.sql.Database

class UserRepositoryImpl(private val database: Database) : UserRepository {
    override suspend fun getUserById(id: Int): User = suspendTransaction(database) {
        UserDao[id].toUser()
    }

    override suspend fun createUser(user: User): Int = suspendTransaction(database) {
        UserDao.new {
            username = user.username
            password = user.password
        }.id.value
    }
}