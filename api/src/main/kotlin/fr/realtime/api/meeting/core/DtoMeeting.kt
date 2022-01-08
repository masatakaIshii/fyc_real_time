package fr.realtime.api.meeting.core

import fr.realtime.api.user.core.DtoUser
import java.time.LocalDateTime
import java.util.*

data class DtoMeeting(
    val id: Long,
    val name: String,
    val description: String,
    val uuid: UUID? = null,
    val createdDateTime: LocalDateTime,
    val creator: DtoUser? = null,
    val isClosed: Boolean = false
)