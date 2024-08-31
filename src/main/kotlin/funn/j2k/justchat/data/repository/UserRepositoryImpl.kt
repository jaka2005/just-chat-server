package funn.j2k.justchat.data.repository

import funn.j2k.justchat.domain.model.User
import funn.j2k.justchat.domain.repository.UserRepository
import org.jetbrains.exposed.sql.Database

class UserRepositoryImpl(private val database: Database) : UserRepository {
    override suspend fun getUserById(id: Int): User {
        TODO("Not yet implemented")
    }

    override suspend fun createUser(user: User): Int {
        TODO("Not yet implemented")
    }
}