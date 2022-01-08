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
                description = "description",
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

    @DisplayName("findAll method")
    @Nested
    inner class FindAll {
        @Test
        fun `should call meeting repository`() {
            jpaMeetingDao.findAll()

            verify(mockMeetingRepository, times(1)).findAll()
        }

        @Test
        fun `when get all meeting entities should return list domain meeting`() {
            val listMeetingEntities = listOf(
                JpaMeeting(
                    id = 35,
                    name = "meeting 35",
                    uuid = UUID.randomUUID(),
                    createdDateTime = LocalDateTime.now(),
                    creatorId = 325,
                    isClosed = true
                )
            )
            `when`(mockMeetingRepository.findAll()).thenReturn(listMeetingEntities)

            val result = jpaMeetingDao.findAll()

            val expected = listMeetingEntities.map(meetingMapper::entityToDomain)
            assertThat(result).isEqualTo(expected)
        }
    }

    @DisplayName("findById method")
    @Nested
    inner class FindById {
        private val meetingId = 38L

        @Test
        fun `should call meeting repository to find meeting by id`() {
            jpaMeetingDao.findById(meetingId)

            verify(mockMeetingRepository, times(1)).findById(meetingId)
        }

        @Test
        fun `when repository found meeting should return found meeting`() {
            val entityMeeting = JpaMeeting(
                meetingId,
                name = "meeting name",
                createdDateTime = LocalDateTime.now(),
                creatorId = 3658L,
                isClosed = false
            )
            `when`(mockMeetingRepository.findById(meetingId)).thenReturn(Optional.of(entityMeeting))

            val result = jpaMeetingDao.findById(meetingId)

            val expectedMeeting = meetingMapper.entityToDomain(entityMeeting)
            assertThat(result).isEqualTo(expectedMeeting)
        }

        @Test
        fun `when repository not found meeting should return null`() {
            `when`(mockMeetingRepository.findById(meetingId)).thenReturn(Optional.empty())

            val result = jpaMeetingDao.findById(meetingId)

            assertThat(result).isNull()
        }
    }
}
