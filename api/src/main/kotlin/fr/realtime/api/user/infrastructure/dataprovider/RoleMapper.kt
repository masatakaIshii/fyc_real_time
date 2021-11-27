package fr.realtime.api.user.infrastructure.dataprovider

import fr.realtime.api.shared.core.mapper.Mapper
import fr.realtime.api.user.core.Role
import org.springframework.stereotype.Component

@Component
class RoleMapper : Mapper<Role, JpaRole> {
    override fun entityToDomain(entity: JpaRole): Role {
        return Role(
            id = entity.id,
            name = entity.name
        )
    }

    override fun domainToEntity(domain: Role): JpaRole {
        return JpaRole(
            id = domain.id,
            name = domain.name
        )
    }
}