package fr.realtime.api.unit.theme.usecase

import fr.realtime.api.meeting.core.Meeting
import fr.realtime.api.meeting.core.MeetingDao
import fr.realtime.api.shared.core.exceptions.ForbiddenException
import fr.realtime.api.shared.core.exceptions.NotFoundException
import fr.realtime.api.theme.core.Theme
import fr.realtime.api.theme.core.ThemeDao
import fr.realtime.api.theme.infrastructure.usecase.SaveTheme
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class SaveThemeTest {
    private lateinit var saveTheme: SaveTheme

    @Mock
    lateinit var mockThemeDao: ThemeDao

    @Mock
    lateinit var mockMeetingDao: MeetingDao

    private val themeName = "theme name"

    private val themeUsername = "username"

    private val meetingId = 35L

    @BeforeEach
    fun init() {
        saveTheme = SaveTheme(mockThemeDao, mockMeetingDao)
    }

    @Test
    fun `when meeting id not found should throw not found exception`() {
        `when`(mockMeetingDao.findById(meetingId)).thenReturn(null)

        assertThatThrownBy{saveTheme.execute(themeName, themeUsername, meetingId)}
                .isExactlyInstanceOf(NotFoundException::class.java)
                .hasMessage("Meeting with id '$meetingId' not found")
    }

    @Test
    fun `when found meeting is close should throw exception`() {
        val foundMeeting = Meeting(
                id = 3,
                name = "meeting name",
                uuid = UUID.randomUUID(),
                createdDateTime = LocalDateTime.now(),
                creatorId = 357,
                isClosed = true)
        `when`(mockMeetingDao.findById(meetingId)).thenReturn(foundMeeting)

        assertThatThrownBy{saveTheme.execute(themeName, themeUsername, meetingId)}
                .isExactlyInstanceOf(ForbiddenException::class.java)
                .hasMessage("Not allowed to create theme in closed meeting")
    }

    @Test
    fun `when theme is saved should return saved theme id`() {
        val savedThemeId = 25L
        val foundMeeting = Meeting(
                id = 3,
                name = "meeting name",
                uuid = UUID.randomUUID(),
                createdDateTime = LocalDateTime.now(),
                creatorId = 357,
                isClosed = false)
        `when`(mockMeetingDao.findById(meetingId)).thenReturn(foundMeeting)
        val themeToSave = Theme(id = 0, name = themeName, username = themeUsername, meetingId = meetingId)
        val savedTheme = themeToSave.copy(id = savedThemeId)
        `when`(mockThemeDao.save(themeToSave)).thenReturn(savedTheme)

        val result = saveTheme.execute(themeName, themeUsername, meetingId)

        assertThat(result).isEqualTo(savedThemeId)
    }
}