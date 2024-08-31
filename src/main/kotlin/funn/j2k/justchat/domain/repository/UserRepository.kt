package funn.j2k.justchat.domain.repository

import funn.j2k.justchat.domain.model.User

interface UserRepository {
    suspend fun getUserById(id: Int): User

    /**
     * create user and return actual user id
     * (the id field in user DTO may have any value, it does not affect to receipt id)
     * @return real user id after adding user to db
     */
    suspend fun createUser(user: User): Int
}