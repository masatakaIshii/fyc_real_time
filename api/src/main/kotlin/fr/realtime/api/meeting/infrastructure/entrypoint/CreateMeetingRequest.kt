package fr.realtime.api.meeting.infrastructure.entrypoint

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class CreateMeetingRequest (
    @field:NotBlank(message = "Meeting name cannot be empty")
    val name: String = "",

    @field:Min(1, message = "Meeting creator id has to be min 1")
    @field:Pattern(regexp = "[0-9]+",  message = "Meeting creator id has to be integer")
    val creatorId: String = ""
)