package fr.realtime.api.theme.usecase

import fr.realtime.api.theme.core.Theme
import org.springframework.stereotype.Service

@Service
class SaveTheme {
    fun execute(theme: Theme): Int {
        return 0
    }
}
