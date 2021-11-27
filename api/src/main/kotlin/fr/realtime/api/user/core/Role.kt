package fr.realtime.api.user.core

data class Role(
    val id: Long = 0,
    val name: RoleName = RoleName.ROLE_USER
)