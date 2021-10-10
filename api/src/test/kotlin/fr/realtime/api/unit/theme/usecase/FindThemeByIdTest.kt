package fr.realtime.api.unit.theme.usecase

import fr.realtime.api.shared.core.exceptions.NotFoundException
import fr.realtime.api.theme.core.Theme
import fr.realtime.api.theme.core.ThemeDao
import fr.realtime.api.theme.infrastructure.usecase.FindThemeById
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class FindThemeByIdTest {
    lateinit var findThemeById: FindThemeById

    @Mock
    lateinit var mockThemeDao: ThemeDao

    @BeforeEach
    fun setup() {
        findThemeById = FindThemeById(mockThemeDao)
    }

    private val themeId = 3658L

    @Test
    fun `when theme dao call to find by id and theme not found should throw not found exception`() {
        `when`(mockThemeDao.findById(themeId)).thenReturn(null)

        assertThatThrownBy{findThemeById.execute(themeId)}
                .isExactlyInstanceOf(NotFoundException::class.java)
                .hasMessage("Theme with id '$themeId' not found")
    }

    @Test
    fun `when theme dao call to find by and theme found should return found theme`() {
        val foundTheme = Theme(id = themeId, name = "found theme", username = "username", meetingId = 2554)
        `when`(mockThemeDao.findById(themeId)).thenReturn(foundTheme)

        val result = findThemeById.execute(themeId)

        assertThat(result).isEqualTo(foundTheme)
    }
}