package fr.realtime.api.theme.infrastructure

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

data class CreateThemeRequest(
        @field:NotBlank(message = "Theme name cannot be empty")
        val name: String = "",

        @field:NotBlank(message = "Theme username cannot be empty")
        val username: String = "",

        @field:Min(1, message = "Theme meeting id has to be min 1")
        val meetingId: String = ""
)