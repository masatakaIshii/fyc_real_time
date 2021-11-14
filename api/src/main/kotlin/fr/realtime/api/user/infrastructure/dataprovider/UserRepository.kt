package fr.realtime.api.user.infrastructure.dataprovider

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<JpaUser, Long> {
    fun findByEmail(email: String): Optional<JpaUser>

    fun existsByEmail(email: String): Boolean
}