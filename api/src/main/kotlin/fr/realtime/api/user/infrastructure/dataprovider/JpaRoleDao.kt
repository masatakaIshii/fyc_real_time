package fr.realtime.api.user.infrastructure.dataprovider

import fr.realtime.api.user.core.Role
import fr.realtime.api.user.core.RoleDao
import fr.realtime.api.user.core.RoleName
import org.springframework.stereotype.Service

@Service
class JpaRoleDao(
    private val roleRepository: RoleRepository,
    private val roleMapper: RoleMapper
) : RoleDao {

    override fun existsByName(roleName: RoleName): Boolean = roleRepository.existsByName(roleName)

    override fun save(role: Role): Role? {
        val roleToSave = roleMapper.domainToEntity(role)
        val savedRole = roleRepository.save(roleToSave)
        return roleMapper.entityToDomain(savedRole)
    }
}