package fr.realtime.api.meeting.usecase

import fr.realtime.api.meeting.core.MeetingDao
import fr.realtime.api.shared.core.exceptions.NotFoundException
import fr.realtime.api.theme.core.Theme
import fr.realtime.api.theme.core.ThemeDao
import org.springframework.stereotype.Service

@Service
class FindMeetingThemes(
        private val meetingDao: MeetingDao,
        private val themeDao: ThemeDao
) {
    @Throws(NotFoundException::class)
    fun execute(meetingId: Long): List<Theme> {
        if (!meetingDao.existsById(meetingId)) {
            throw NotFoundException("Meeting with id '$meetingId' not found")
        }

        return themeDao.findAllByMeetingId(meetingId)
    }
}
