package fr.realtime.api.meeting.usecase

import fr.realtime.api.meeting.core.Meeting
import fr.realtime.api.meeting.core.MeetingDao
import fr.realtime.api.shared.core.exceptions.NotFoundException
import fr.realtime.api.shared.core.utils.DateHelper
import fr.realtime.api.shared.core.utils.UUIDHelper
import fr.realtime.api.user.core.UserDao
import org.springframework.stereotype.Service

@Service
class SaveMeeting(
    private val meetingDao: MeetingDao,
    private val userDao: UserDao,
    private val uuidHelper: UUIDHelper,
    private val dateHelper: DateHelper
) {
    fun execute(name: String, description: String, creatorId: Long): Long {
        if (!userDao.existsById(creatorId)) {
            throw NotFoundException("USER_NOT_FOUND: User with id $creatorId not found")
        }

        val newUUID = uuidHelper.generateRandomUUID()
        val now = dateHelper.localDateTimeNow()

        val meetingToSave = Meeting(
            id = 0,
            name = name,
            description = description,
            uuid = newUUID,
            createdDateTime = now,
            creatorId = creatorId
        )
        val savedMeeting = meetingDao.save(meetingToSave)
        return savedMeeting.id
    }
}