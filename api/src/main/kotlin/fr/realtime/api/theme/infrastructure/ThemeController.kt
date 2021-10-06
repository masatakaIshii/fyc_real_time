package fr.realtime.api.theme.infrastructure

import fr.realtime.api.theme.core.Theme
import fr.realtime.api.theme.usecase.SaveTheme
import org.slf4j.LoggerFactory
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/theme")
@Validated
class ThemeController(val saveTheme: SaveTheme) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun createTheme(@Valid @RequestBody request: CreateThemeRequest) {
        logger.info("Create theme with name '${request.name}' in meeting with id '${request.meetingId}'")

        saveTheme.execute(Theme(request.name, request.username, request.meetingId.toLong()))
    }
}