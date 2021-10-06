package fr.realtime.api.theme.usecase

import fr.realtime.api.theme.core.Theme
import fr.realtime.api.theme.core.ThemeDao
import org.springframework.stereotype.Service

@Service
class SaveTheme(val themeDao: ThemeDao) {
    fun execute(theme: Theme): Int {
        themeDao.findLastOneByUsername(theme.username)
        return 0
    }
}
