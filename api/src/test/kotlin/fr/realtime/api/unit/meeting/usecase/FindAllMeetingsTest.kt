package fr.realtime.api.unit.meeting.usecase

import fr.realtime.api.meeting.core.Meeting
import fr.realtime.api.meeting.core.MeetingDao
import fr.realtime.api.meeting.usecase.FindAllMeetings
import fr.realtime.api.user.core.UserDao
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
internal class FindAllMeetingsTest {
    lateinit var findAllMeetings: FindAllMeetings

    @Mock
    lateinit var mockMeetingDao: MeetingDao

    @Mock
    lateinit var mockUserDao: UserDao

    @BeforeEach
    fun setup() {
        findAllMeetings = FindAllMeetings(mockMeetingDao, mockUserDao)
    }

    @Test
    fun `should call find all of meeting dao`() {
        findAllMeetings.execute()

        verify(mockMeetingDao, times(1)).findAll()
    }

    @Test
    fun `when found all meetings by dao should return found meetings`() {
        val listMeetings = listOf(
                Meeting(id = 63, name = "meeting 63", createdDateTime = LocalDateTime.now(), creatorId = 25, isClosed = true),
                Meeting(id = 3, name = "meeting 3", createdDateTime = LocalDateTime.now(), creatorId = 35, isClosed = false)
        )
        `when`(mockMeetingDao.findAll()).thenReturn(listMeetings)

        val result = findAllMeetings.execute()

        assertThat(result).isEqualTo(listMeetings)
    }
}