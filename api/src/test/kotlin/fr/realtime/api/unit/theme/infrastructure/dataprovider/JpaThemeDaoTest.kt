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
import java.util.*

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

    @DisplayName("findById method")
    @Nested
    inner class FindByIdMethod {
        private val themeId = 54L

        @Test
        fun `should call repository to find theme by id`() {
            jpaThemeDao.findById(themeId)

            verify(mockThemeRepository, times(1)).findById(themeId)
        }

        @Test
        fun `when theme not found should return null`() {
            `when`(mockThemeRepository.findById(themeId)).thenReturn(Optional.empty())

            val result = jpaThemeDao.findById(themeId)

            assertThat(result).isNull()
        }

        @Test
        fun `when theme found should return found theme`() {
            val foundEntityTheme = JpaTheme(
                    id = themeId,
                    name = "theme name",
                    username = "username",
                    meetingId = 365
            )
            `when`(mockThemeRepository.findById(themeId)).thenReturn(Optional.of(foundEntityTheme))

            val result = jpaThemeDao.findById(themeId)

            val expectedTheme = themeMapper.entityToDomain(foundEntityTheme)
            assertThat(result).isEqualTo(expectedTheme)
        }
    }

    @DisplayName("findAllByMeetingId method")
    @Nested
    inner class FindAllByMeetingId {
        private val meetingId = 354L

        @Test
        fun `should call theme repository to find all themes by meeting id`() {
            jpaThemeDao.findAllByMeetingId(meetingId)

            verify(mockThemeRepository, times(1)).findAllByMeetingId(meetingId)
        }

        @Test
        fun `when theme repository return list theme entities should return list theme domains`() {
            val listThemes = listOf(
                    JpaTheme(654, "name654", "username654", meetingId),
                    JpaTheme(254, "theme254", "username254", meetingId)
            )
            `when`(mockThemeRepository.findAllByMeetingId(meetingId)).thenReturn(listThemes)

            val result = jpaThemeDao.findAllByMeetingId(meetingId)

            val expected = listThemes.map(themeMapper::entityToDomain)
            assertThat(result).isEqualTo(expected)
        }
    }
}