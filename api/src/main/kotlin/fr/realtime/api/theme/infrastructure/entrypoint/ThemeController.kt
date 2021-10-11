package fr.realtime.api.theme.infrastructure.entrypoint

import fr.realtime.api.theme.core.Theme
import fr.realtime.api.theme.infrastructure.usecase.FindThemeById
import fr.realtime.api.theme.infrastructure.usecase.SaveTheme
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.created
import org.springframework.http.ResponseEntity.ok
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import javax.validation.Valid
import javax.validation.constraints.Min
import javax.validation.constraints.Pattern

@RestController
@RequestMapping("/api/theme")
@Validated
class ThemeController(
        private val saveTheme: SaveTheme,
        private val findThemeById: FindThemeById
) {
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

    @GetMapping("{themeId}")
    fun findById(
            @Min(1)
            @Pattern(regexp = "[0-9]+",  message = "Theme id has to be integer")
            @PathVariable themeId: String): ResponseEntity<Theme> {
        val foundTheme = findThemeById.execute(themeId.toLong())
        return ok(foundTheme)
    }
}