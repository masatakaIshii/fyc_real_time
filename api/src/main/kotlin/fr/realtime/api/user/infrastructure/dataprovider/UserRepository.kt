package fr.realtime.api.user.infrastructure.dataprovider

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<JpaUser, Long> {
}