package fr.realtime.api.unit.theme.usecase

import fr.realtime.api.theme.core.Theme
import fr.realtime.api.theme.core.ThemeDao
import fr.realtime.api.theme.usecase.SaveTheme
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class SaveThemeTest {
    private lateinit var saveTheme: SaveTheme

    @Mock
    lateinit var mockThemeDao: ThemeDao

    @BeforeEach
    fun init() {
        saveTheme = SaveTheme(mockThemeDao)
    }

    @Test
    fun `should find last theme of user by username`() {
        val givenTheme = Theme(name = "theme", username = "the username", meetingId = 654L)

        saveTheme.execute(givenTheme)

        verify(mockThemeDao, times(1)).findLastOneByUsername(givenTheme.username)
    }
//
//    @Test
//    fun `when theme found and `
}