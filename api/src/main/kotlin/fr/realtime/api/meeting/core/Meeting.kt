package fr.realtime.api.meeting.core

import java.util.*

data class Meeting (val id: Long, val name: String, val uuid: UUID, val createdDate: Date, val creatorId: Long)