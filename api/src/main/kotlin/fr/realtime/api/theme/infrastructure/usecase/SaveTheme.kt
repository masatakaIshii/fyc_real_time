package fr.realtime.api.theme.infrastructure.usecase

import fr.realtime.api.meeting.core.MeetingDao
import fr.realtime.api.shared.core.exceptions.ForbiddenException
import fr.realtime.api.shared.core.exceptions.NotFoundException
import fr.realtime.api.theme.core.Theme
import fr.realtime.api.theme.core.ThemeDao
import org.springframework.stereotype.Service

@Service
class SaveTheme(
        private val themeDao: ThemeDao,
        private val meetingDao: MeetingDao
) {
    fun execute(name: String, username: String, meetingId: Long): Long {
        val foundMeeting = meetingDao.findById(meetingId)
                ?: throw NotFoundException("Meeting with id '$meetingId' not found")
        if (foundMeeting.isClosed) {
            throw ForbiddenException("Not allowed to create theme in closed meeting")
        }
        val themeToSave = Theme(
                name = name,
                username = username,
                meetingId = meetingId)

        return themeDao.save(themeToSave).id
    }
}
