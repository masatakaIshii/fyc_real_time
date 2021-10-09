package fr.realtime.api.unit.meeting.infrastructure.dataprovider

import fr.realtime.api.meeting.core.Meeting
import fr.realtime.api.meeting.infrastructure.dataprovider.JpaMeeting
import fr.realtime.api.meeting.infrastructure.dataprovider.JpaMeetingDao
import fr.realtime.api.meeting.infrastructure.dataprovider.MeetingMapper
import fr.realtime.api.meeting.infrastructure.dataprovider.MeetingRepository
import org.assertj.core.api.Assertions.assertThat
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
internal class JpaMeetingDaoTest {
    lateinit var jpaMeetingDao: JpaMeetingDao

    @Mock
    lateinit var mockMeetingRepository: MeetingRepository
    val meetingMapper = MeetingMapper()

    @BeforeEach
    fun setup() {
        jpaMeetingDao = JpaMeetingDao(mockMeetingRepository, meetingMapper)
    }

    @DisplayName("save method")
    @Nested
    inner class Save {

        @Test
        fun `when meeting saved should return saved meeting`() {
            val uuid = UUID.randomUUID()
            val createdDateTime = LocalDateTime.now()
            val creatorId = 354L
            val meetingToSave = Meeting(
                    0L,
                    name = "new meeting",
                    uuid = uuid,
                    createdDateTime = createdDateTime,
                    creatorId = creatorId
            )
            val entityMeetingToSave = meetingMapper.domainToEntity(meetingToSave)
            val entitySavedMeeting = entityMeetingToSave.copy(id = 3654L)
            `when`(mockMeetingRepository.save(entityMeetingToSave)).thenReturn(entitySavedMeeting)

            val result = jpaMeetingDao.save(meetingToSave)

            val expected = meetingMapper.entityToDomain(entitySavedMeeting)
            assertThat(result).isEqualTo(expected)
        }
    }
}
