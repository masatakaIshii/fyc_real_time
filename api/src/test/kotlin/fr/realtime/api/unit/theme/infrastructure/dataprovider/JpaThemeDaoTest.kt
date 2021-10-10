package fr.realtime.api.unit.theme.infrastructure.dataprovider

import fr.realtime.api.theme.core.Theme
import fr.realtime.api.theme.infrastructure.dataprovider.JpaTheme
import fr.realtime.api.theme.infrastructure.dataprovider.JpaThemeDao
import fr.realtime.api.theme.infrastructure.dataprovider.ThemeMapper
import fr.realtime.api.theme.infrastructure.dataprovider.ThemeRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class JpaThemeDaoTest {
    lateinit var jpaThemeDao: JpaThemeDao

    @Mock
    lateinit var mockThemeRepository: ThemeRepository

    val themeMapper: ThemeMapper = ThemeMapper()

    @BeforeEach
    fun setup() {
        jpaThemeDao = JpaThemeDao(mockThemeRepository, themeMapper)
    }

    @DisplayName("save method")
    @Nested
    inner class SaveMethod {
        @Test
        fun `when theme saved should return saved theme`() {
            val themeDomainToSave = Theme(
                    name = "theme to save",
                    username = "username",
                    meetingId = 357L)
            val themeEntityToSave = JpaTheme(
                    name = themeDomainToSave.name,
                    username = themeDomainToSave.username,
                    meetingId = themeDomainToSave.meetingId)
            val savedThemeEntityId = 35L
            val savedThemeEntity = themeEntityToSave.copy(id = savedThemeEntityId)
            `when`(mockThemeRepository.save(themeEntityToSave)).thenReturn(savedThemeEntity)

            val result = jpaThemeDao.save(themeDomainToSave)

            val expectedTheme = themeMapper.entityToDomain(savedThemeEntity)
            assertThat(result).isEqualTo(expectedTheme)
        }
    }
}