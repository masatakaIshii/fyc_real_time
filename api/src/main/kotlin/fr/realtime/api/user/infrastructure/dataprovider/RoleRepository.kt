package fr.realtime.api.user.infrastructure.dataprovider

import fr.realtime.api.user.core.RoleName
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RoleRepository : JpaRepository<JpaRole, Long> {
    fun findByName(name: RoleName): Optional<JpaRole>

    fun existsByName(name: RoleName): Boolean

}