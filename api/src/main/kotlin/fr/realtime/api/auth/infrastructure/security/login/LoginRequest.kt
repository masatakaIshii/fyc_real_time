package fr.realtime.api.auth.infrastructure.security.login

data class LoginRequest(
    val email: String,
    val password: String
)