package fr.realtime.api.meeting.infrastructure.entrypoint

import javax.validation.constraints.NotBlank

data class CreateMeetingRequest (
    @field:NotBlank(message = "Meeting name cannot be empty")
    val name: String = "",
)