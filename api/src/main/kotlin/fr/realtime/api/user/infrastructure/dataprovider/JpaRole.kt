package fr.realtime.api.user.infrastructure.dataprovider

import fr.realtime.api.user.core.RoleName
import javax.persistence.*

@Entity(name = "role")
data class JpaRole(
    @get:Id
    @get:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    @get:Column(nullable = false)
    @get:Enumerated(EnumType.STRING)
    var name: RoleName
)