package fr.realtime.api.user.infrastructure.dataprovider

import javax.persistence.*

@Entity(name = "user")
data class JpaUser(
        @get:Id
        @get:GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
        var id: Long,

        @get:Column(nullable = false)
        var name: String,

        @get:Column(nullable = false)
        var password: String = "",

        @get:Column(unique = true, nullable = false)
        var email: String
)