package fr.realtime.api.theme.infrastructure

import fr.realtime.api.theme.core.Theme
import fr.realtime.api.theme.core.ThemeDao
import org.springframework.stereotype.Service

@Service
class JpaThemeDao : ThemeDao {
    override fun findLastOneByUsername(username: String): Theme {
        TODO("Not yet implemented")
    }

    override fun save(theme: Theme): Theme {
        TODO("Not yet implemented")
    }
}