package fr.realtime.api.meeting.usecase

import fr.realtime.api.meeting.core.DtoMeeting
import fr.realtime.api.meeting.core.MeetingDao
import fr.realtime.api.user.core.DtoUser
import fr.realtime.api.user.core.UserDao
import org.springframework.stereotype.Service

@Service
class FindAllMeetings(
    private val meetingDao: MeetingDao,
    private val userDao: UserDao
) {
    fun execute(): List<DtoMeeting> = meetingDao.findAll().map {
        val creator = userDao.findById(it.creatorId)
        val dtoCreator = creator?.let { DtoUser(creator.id, creator.name, creator.email) }
        DtoMeeting(it.id, it.name, it.uuid, it.createdDateTime, dtoCreator, it.isClosed)
    }
}