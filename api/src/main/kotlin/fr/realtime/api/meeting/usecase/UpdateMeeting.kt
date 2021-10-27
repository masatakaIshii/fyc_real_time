package fr.realtime.api.meeting.usecase

import fr.realtime.api.meeting.core.MeetingDao
import fr.realtime.api.shared.core.exceptions.NotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class UpdateMeeting(private val meetingDao: MeetingDao) {
    fun execute(meetingId: Long, name: String?, uuid: UUID?, isClosed: Boolean?) {
        val meeting = meetingDao.findById(meetingId)
                ?: throw NotFoundException("Meeting with id '$meetingId' not found")

        val meetingToSave = meeting.copy(
                name = name ?: meeting.name,
                uuid = uuid ?: meeting.uuid,
                isClosed = isClosed ?: meeting.isClosed
        )

        meetingDao.save(meetingToSave)
    }
}