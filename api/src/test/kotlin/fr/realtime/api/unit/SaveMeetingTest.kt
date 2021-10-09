package fr.realtime.api.unit

import fr.realtime.api.meeting.core.Meeting
import fr.realtime.api.meeting.core.MeetingDao
import fr.realtime.api.meeting.usecase.SaveMeeting
import fr.realtime.api.user.core.UserDao
import fr.realtime.api.shared.core.exceptions.NotFoundException
import fr.realtime.api.shared.core.utils.DateHelper
import fr.realtime.api.shared.core.utils.UUIDHelper
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class SaveMeetingTest {
    private lateinit var saveMeeting: SaveMeeting

    @Mock
    lateinit var mockMeetingDao: MeetingDao

    @Mock
    lateinit var mockUserDao: UserDao

    @Mock
    lateinit var mockUUIDHelper: UUIDHelper

    @Mock
    lateinit var mockDateHelper: DateHelper

    @BeforeEach
    fun setup() {
        saveMeeting = SaveMeeting(mockMeetingDao, mockUserDao, mockUUIDHelper, mockDateHelper)
    }

    private val creatorId = 358L

    @Test
    fun `when user not exists by creator id should throw exception`() {
        `when`(mockUserDao.existsById(creatorId)).thenReturn(false)

        assertThatThrownBy { saveMeeting.execute("new meeting name", creatorId) }
                .isExactlyInstanceOf(NotFoundException::class.java)
    }

    @DisplayName("when user exists")
    @Nested
    inner class WhenUserExists {
        @BeforeEach
        fun setup() {
            `when`(mockUserDao.existsById(creatorId)).thenReturn(true)
        }

        @Test
        fun `when meeting saved should return saved meeting id`() {
            val generateUUID = UUID.fromString("32049fd4-2867-11ec-9621-0242ac130002")
            `when`(mockUUIDHelper.generateRandomUUID()).thenReturn(generateUUID)
            val localDateTimeNow = LocalDateTime.now()
            `when`(mockDateHelper.localDateTimeNow()).thenReturn(localDateTimeNow)
            val meetingToSave = Meeting(
                    0,
                    name = "new meeting name",
                    uuid = generateUUID,
                    creatorId = creatorId,
                    createdDateTime = localDateTimeNow
            )
            val newMeetingId = 65L
            val savedMeeting = meetingToSave.copy(id = newMeetingId)

            `when`(mockMeetingDao.save(meetingToSave)).thenReturn(savedMeeting)

            val result = saveMeeting.execute("new meeting name", creatorId)

            assertThat(result).isEqualTo(newMeetingId)
        }
    }

}