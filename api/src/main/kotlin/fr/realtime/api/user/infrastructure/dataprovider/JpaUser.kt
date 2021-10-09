package fr.realtime.api.user.infrastructure.dataprovider

import javax.persistence.*

@Entity(name = "user")
data class JpaUser(
        @get:Id
        @get:GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
        var id: Long,

        var name: String,

        var password: String
)