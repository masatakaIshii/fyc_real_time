package fr.realtime.api.meeting.infrastructure.dataprovider

import fr.realtime.api.meeting.core.Meeting
import fr.realtime.api.meeting.core.MeetingDao
import org.springframework.stereotype.Service

@Service
class JpaMeetingDao(
        private val meetingRepository: MeetingRepository,
        private val meetingMapper: MeetingMapper
) : MeetingDao {

    override fun save(meeting: Meeting): Meeting {
        val meetingToSave = meetingMapper.domainToEntity(meeting)
        val savedMeeting = meetingRepository.save(meetingToSave)
        return meetingMapper.entityToDomain(savedMeeting)
    }

    override fun findAll(): List<Meeting> = meetingRepository
            .findAll()
            .map(meetingMapper::entityToDomain)

    override fun findById(meetingId: Long): Meeting? = meetingRepository
            .findById(meetingId)
            .map(meetingMapper::entityToDomain)
            .orElse(null)

    override fun existsById(meetingId: Long): Boolean = meetingRepository.existsById(meetingId)
    override fun deleteById(meetingId: Long) {
        meetingRepository.deleteById(meetingId)
    }
}