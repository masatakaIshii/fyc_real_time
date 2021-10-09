package fr.realtime.api.meeting.usecase

import fr.realtime.api.meeting.core.Meeting
import fr.realtime.api.meeting.core.MeetingDao
import org.springframework.stereotype.Service

@Service
class FindAllMeetings(private val meetingDao: MeetingDao) {
    fun execute(): List<Meeting> = meetingDao.findAll()
}