package fr.realtime.api.theme.infrastructure.usecase

import fr.realtime.api.shared.core.exceptions.NotFoundException
import fr.realtime.api.theme.core.Theme
import fr.realtime.api.theme.core.ThemeDao
import org.springframework.stereotype.Service

@Service
class FindThemeById(private val themeDao: ThemeDao) {
    fun execute(themeId: Long): Theme = themeDao.findById(themeId)
            ?: throw NotFoundException("Theme with id '$themeId' not found")
}
