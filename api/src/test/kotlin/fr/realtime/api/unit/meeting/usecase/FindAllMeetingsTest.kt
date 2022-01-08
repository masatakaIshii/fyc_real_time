package fr.realtime.api.unit.meeting.usecase

import fr.realtime.api.meeting.core.DtoMeeting
import fr.realtime.api.meeting.core.Meeting
import fr.realtime.api.meeting.core.MeetingDao
import fr.realtime.api.meeting.usecase.FindAllMeetings
import fr.realtime.api.user.core.DtoUser
import fr.realtime.api.user.core.User
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
        val localDateTime = LocalDateTime.now()
        val listMeetings = listOf(
            Meeting(
                id = 63,
                name = "meeting 63",
                description = "description meeting 63",
                createdDateTime = localDateTime,
                creatorId = 35,
                isClosed = true
            ),
            Meeting(
                id = 3,
                name = "meeting 3",
                description = "description meeting 3",
                createdDateTime = localDateTime,
                creatorId = 35,
                isClosed = false
            )
        )
        `when`(mockMeetingDao.findAll()).thenReturn(listMeetings)
        `when`(mockUserDao.findById(35)).thenReturn(User(35, "user", "password", "user@user.com", setOf()))

        val result = findAllMeetings.execute()

        val expectedList = listOf(
            DtoMeeting(
                id = 63,
                name = "meeting 63",
                description = "description meeting 63",
                createdDateTime = localDateTime,
                creator = DtoUser(35, "user", "user@user.com"),
                isClosed = true
            ),
            DtoMeeting(
                id = 3,
                name = "meeting 3",
                description = "description meeting 3",
                createdDateTime = localDateTime,
                creator = DtoUser(35, "user", "user@user.com"),
                isClosed = false
            )
        )
        assertThat(result).isEqualTo(expectedList)
    }
}