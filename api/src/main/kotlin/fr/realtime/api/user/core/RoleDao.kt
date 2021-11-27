package fr.realtime.api.user.core

interface RoleDao {
    fun existsByName(roleName: RoleName): Boolean

    fun save(role: Role): Role?
}