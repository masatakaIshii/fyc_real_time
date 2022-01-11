package fr.realtime.api.meeting.usecase

import fr.realtime.api.meeting.core.MeetingDao
import fr.realtime.api.shared.core.exceptions.ForbiddenException
import org.springframework.stereotype.Service
import fr.realtime.api.shared.core.exceptions.NotFoundException as NotFoundException1

@Service
class DeleteMeetingById(private val meetingDao: MeetingDao) {
    @Throws(NotFoundException1::class, ForbiddenException::class)
    fun execute(meetingId: Long, userId: Long) {
        val foundMeeting = meetingDao.findById(meetingId) ?: throw NotFoundException1("Meeting with id $meetingId not found")
        if (foundMeeting.creatorId != userId) throw ForbiddenException("User $userId forbidden to delete meeting $meetingId")

        meetingDao.deleteById(meetingId)
    }
}