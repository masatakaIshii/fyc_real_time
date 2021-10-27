package fr.realtime.api.unit.meeting.usecase

import fr.realtime.api.meeting.core.Meeting
import fr.realtime.api.meeting.core.MeetingDao
import fr.realtime.api.meeting.usecase.UpdateMeeting
import fr.realtime.api.shared.core.exceptions.NotFoundException
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
internal class UpdateMeetingTest {
    lateinit var updateMeeting: UpdateMeeting

    @Mock
    lateinit var mockMeetingDao: MeetingDao

    @BeforeEach
    fun setup() {
        updateMeeting = UpdateMeeting(mockMeetingDao)
    }

    private var meetingId = 354L


    @Test
    fun `when found meeting by id return null should throw exception`() {
        `when`(mockMeetingDao.findById(meetingId)).thenReturn(null)

        assertThatThrownBy {
            updateMeeting.execute(meetingId, "new meeting name", UUID.randomUUID(), false)
        }
                .isExactlyInstanceOf(NotFoundException::class.java)
                .hasMessage("Meeting with id '$meetingId' not found")
    }

    @Test
    fun `when meeting found by id should save meeting with new name, uuid and isClosed`() {
        val savedMeeting = Meeting(
                id = meetingId,
                name = "meeting name",
                uuid = UUID.randomUUID(),
                isClosed = false,
                creatorId = 7,
                createdDateTime = LocalDateTime.now()
        )
        `when`(mockMeetingDao.findById(meetingId)).thenReturn(savedMeeting)
        val uuid = UUID.randomUUID()

        updateMeeting.execute(meetingId, "new meeting name", uuid, true)

        val expectedMeetingToSave = savedMeeting.copy(
                name = "new meeting name",
                uuid = uuid,
                isClosed = true
        )
        verify(mockMeetingDao, times(1)).save(expectedMeetingToSave)
    }

    @Test
    fun `when params id and name are defined and others params are null should update only meeting name`() {
        val savedMeeting = Meeting(
                id = meetingId,
                name = "meeting name",
                uuid = UUID.randomUUID(),
                isClosed = false,
                creatorId = 7,
                createdDateTime = LocalDateTime.now()
        )
        `when`(mockMeetingDao.findById(meetingId)).thenReturn(savedMeeting)

        updateMeeting.execute(meetingId, "new meeting name", null, null)

        val expectedMeetingToSave = savedMeeting.copy(
                name = "new meeting name",
                uuid = savedMeeting.uuid,
                isClosed = savedMeeting.isClosed
        )
        verify(mockMeetingDao, times(1)).save(expectedMeetingToSave)
    }

    @Test
    fun `when params id and uuid are defined and others params are null should update only meeting uuid`() {
        val savedMeeting = Meeting(
                id = meetingId,
                name = "meeting name",
                uuid = UUID.randomUUID(),
                isClosed = false,
                creatorId = 7,
                createdDateTime = LocalDateTime.now()
        )
        `when`(mockMeetingDao.findById(meetingId)).thenReturn(savedMeeting)
        val newUUID = UUID.randomUUID()

        updateMeeting.execute(meetingId, null, newUUID, null)

        val expectedMeetingToSave = savedMeeting.copy(
                name = "meeting name",
                uuid = newUUID,
                isClosed = savedMeeting.isClosed
        )
        verify(mockMeetingDao, times(1)).save(expectedMeetingToSave)
    }

    @Test
    fun `when params id and isClosed are defined and others params are null should update only meeting isClosed param`() {
        val savedMeeting = Meeting(
                id = meetingId,
                name = "meeting name",
                uuid = UUID.randomUUID(),
                isClosed = false,
                creatorId = 7,
                createdDateTime = LocalDateTime.now()
        )
        `when`(mockMeetingDao.findById(meetingId)).thenReturn(savedMeeting)

        updateMeeting.execute(meetingId, null, null, true)

        val expectedMeetingToSave = savedMeeting.copy(
                name = "meeting name",
                uuid = savedMeeting.uuid,
                isClosed = true
        )
        verify(mockMeetingDao, times(1)).save(expectedMeetingToSave)
    }
}