package fr.realtime.api.auth.infrastructure.entrypoint

import fr.realtime.api.auth.usecase.RegisterUser
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping("api/auth")
class AuthController(private val registerUser: RegisterUser) {

    @PostMapping("register")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<String> {
        registerUser.execute(
            request.name,
            request.email,
            request.password
        )
        val loginUri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("api/auth/login")
            .buildAndExpand()
            .toUri()

        return created(loginUri).build()
    }
}