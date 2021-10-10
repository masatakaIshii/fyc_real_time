package fr.realtime.api.theme.infrastructure.entrypoint

import fr.realtime.api.theme.infrastructure.usecase.SaveTheme
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.created
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/theme")
@Validated
class ThemeController(val saveTheme: SaveTheme) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun createTheme(@Valid @RequestBody request: CreateThemeRequest): ResponseEntity<URI> {
        logger.info("Create theme with name '${request.name}' in meeting with id '${request.meetingId}'")

        val newThemeId = saveTheme.execute(request.name, request.username, request.meetingId.toLong())

        val uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(newThemeId)
                .toUri()
        return created(uri).build()
    }
}