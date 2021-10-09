package fr.realtime.api.meeting.infrastructure.dataprovider

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity(name = "meeting")
data class JpaMeeting(
        @get:Id
        @get:GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
        var id: Long,

        var name: String,

        var uuid: UUID?,

        @get:Column(name = "created_date_time")
        var createdDateTime: LocalDateTime,

        @get:Column(name = "creator_id")
        var creatorId: Long,

        @get:Column(name = "is_closed")
        var isClosed : Boolean = false
)