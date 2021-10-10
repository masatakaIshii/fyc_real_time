package fr.realtime.api.theme.infrastructure.dataprovider

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "theme")
data class JpaTheme(
        @get:Id
        @get:GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
        var id: Long = 0,

        var name: String,

        var username: String,

        var meetingId: Long
)
