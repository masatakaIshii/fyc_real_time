package fr.realtime.api.theme.infrastructure.dataprovider

import fr.realtime.api.theme.core.Theme
import fr.realtime.api.theme.core.ThemeDao
import org.springframework.stereotype.Service

@Service
class JpaThemeDao(
        private val themeRepository: ThemeRepository,
        private val themeMapper: ThemeMapper
) : ThemeDao {
    override fun save(theme: Theme): Theme {
        val themeToSave = themeMapper.domainToEntity(theme)
        val savedTheme = themeRepository.save(themeToSave)
        return themeMapper.entityToDomain(savedTheme)
    }
}