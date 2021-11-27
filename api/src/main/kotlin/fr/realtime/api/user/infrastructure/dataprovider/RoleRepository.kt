package fr.realtime.api.user.infrastructure.dataprovider

import fr.realtime.api.user.core.RoleName
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<JpaRole, Long> {
    fun existsByName(name: RoleName): Boolean
}