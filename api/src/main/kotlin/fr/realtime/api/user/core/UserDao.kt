package fr.realtime.api.user.core

interface UserDao {
    fun findById(userId: Long): User?

    fun existsById(userId: Long): Boolean

    fun save(user: User): User
}