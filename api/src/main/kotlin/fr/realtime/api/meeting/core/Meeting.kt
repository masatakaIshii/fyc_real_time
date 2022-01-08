package fr.realtime.api.meeting.core

import java.time.LocalDateTime
import java.util.*

data class Meeting(
        val id: Long,
        val name: String,
        val description: String,
        val uuid: UUID? = null,
        val createdDateTime: LocalDateTime,
        val creatorId: Long,
        val isClosed: Boolean = false
)