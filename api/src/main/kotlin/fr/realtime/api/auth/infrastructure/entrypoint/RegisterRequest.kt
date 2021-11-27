package fr.realtime.api.auth.infrastructure.entrypoint

import javax.validation.constraints.NotBlank

class RegisterRequest (
    @field:NotBlank(message ="Name cannot be empty")
    val name: String = "",

    @field:NotBlank(message = "Email cannot be empty")
    val email: String = "",

    @field:NotBlank(message = "Password cannot be empty")
    val password: String = "",
)