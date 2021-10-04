package fr.realtime.api.theme.infrastructure

import javax.validation.constraints.NotBlank

data class CreateThemeRequest(
        @field:NotBlank(message = "Theme name cannot be empty")
        val name: String = "",

        @field:NotBlank(message = "Theme username cannot be empty")
        val username: String = ""
)