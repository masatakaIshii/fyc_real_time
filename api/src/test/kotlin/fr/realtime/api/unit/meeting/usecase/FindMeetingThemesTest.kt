package fr.realtime.api.unit.meeting.usecase

import fr.realtime.api.meeting.core.MeetingDao
import fr.realtime.api.meeting.usecase.FindMeetingThemes
import fr.realtime.api.shared.core.exceptions.NotFoundException
import fr.realtime.api.theme.core.Theme
import fr.realtime.api.theme.core.ThemeDao
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class FindMeetingThemesTest {
    private lateinit var findMeetingThemes: FindMeetingThemes

    @Mock
    lateinit var mockMeetingDao: MeetingDao

    @Mock
    lateinit var mockThemeDao: ThemeDao

    @BeforeEach
    fun setup() {
        findMeetingThemes = FindMeetingThemes(mockMeetingDao, mockThemeDao)
    }

    private val meetingId = 3684L

    @Test
    fun `when meeting not exists by id should throw not found exception`() {
        `when`(mockMeetingDao.existsById(meetingId)).thenReturn(false)

        assertThatThrownBy{findMeetingThemes.execute(meetingId)}
                .isExactlyInstanceOf(NotFoundException::class.java)
                .hasMessage("Meeting with id '$meetingId' not found")
    }

    @Test
    fun `when meeting exists by id should get all theme of meeting by meeting id`() {
        `when`(mockMeetingDao.existsById(meetingId)).thenReturn(true)

        findMeetingThemes.execute(meetingId)

        verify(mockThemeDao, times(1)).findAllByMeetingId(meetingId)
    }

    @Test
    fun `when get all themes of meeting by meeting id should return list themes`() {
        `when`(mockMeetingDao.existsById(meetingId)).thenReturn(true)
        val listThemes = listOf(
                Theme(3, "theme 3", "username of theme 3", meetingId),
                Theme(36, "theme 36", "user theme 36", meetingId)
        )
        `when`(mockThemeDao.findAllByMeetingId(meetingId)).thenReturn(listThemes)

        val result = findMeetingThemes.execute(meetingId)

        assertThat(result).isEqualTo(listThemes)
    }
}