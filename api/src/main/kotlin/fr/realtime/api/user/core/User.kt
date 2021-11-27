package fr.realtime.api.user.core

data class User(
    val id: Long,
    val name: String,
    val password: String?,
    val email: String?,
    val roles: Set<Role> = setOf()
)