package fr.realtime.api.meeting.infrastructure.entrypoint

import java.util.*

data class UpdateMeetingRequest(
        val name: String? = null,

        @field:fr.realtime.api.shared.core.validation.UUID("Meeting uuid not correct")
        val uuid: UUID? = null,
        val isClosed: Boolean? = null
)