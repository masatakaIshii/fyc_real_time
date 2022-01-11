package fr.realtime.api.unit.meeting.usecase

import fr.realtime.api.meeting.core.Meeting
import fr.realtime.api.meeting.core.MeetingDao
import fr.realtime.api.meeting.usecase.DeleteMeetingById
import fr.realtime.api.shared.core.exceptions.ForbiddenException
import fr.realtime.api.shared.core.exceptions.NotFoundException
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.web.bind.annotation.CrossOrigin
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class DeleteMeetingByIdTest {
    private lateinit var deleteMeetingById: DeleteMeetingById

    @Mock
    lateinit var mockMeetingDao: MeetingDao

    @BeforeEach
    fun setup() {
        deleteMeetingById = DeleteMeetingById(mockMeetingDao)
    }

    private val meetingId = 3L

    private val userId = 65L

    @Test
    fun `when meeting dao not found should throw not found exception`() {
        `when`(mockMeetingDao.findById(meetingId)).thenReturn(null)

        assertThatThrownBy { deleteMeetingById.execute(meetingId, userId) }
            .isExactlyInstanceOf(NotFoundException::class.java)
            .hasMessage("Meeting with id $meetingId not found")
    }

    @Test
    fun `when meeting found but creator id is not param user id should throw forbidden exception`() {
        val foundMeeting =
            Meeting(meetingId, "meeting name", "description", UUID.randomUUID(), LocalDateTime.now(), userId + 1)
        `when`(mockMeetingDao.findById(meetingId)).thenReturn(foundMeeting)

        assertThatThrownBy { deleteMeetingById.execute(meetingId, userId) }
            .isExactlyInstanceOf(ForbiddenException::class.java)
            .hasMessage("User $userId forbidden to delete meeting $meetingId")
    }

    @Test
    fun `when meeting found and creator id is user id should delete meeting by id`() {
        val foundMeeting =
            Meeting(meetingId, "meeting name", "description", UUID.randomUUID(), LocalDateTime.now(), userId)
        `when`(mockMeetingDao.findById(meetingId)).thenReturn(foundMeeting)

        deleteMeetingById.execute(meetingId, userId)

        verify(mockMeetingDao, times(1)).deleteById(meetingId)
    }
}