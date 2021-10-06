package fr.realtime.api.meeting.usecase

import org.springframework.stereotype.Service

@Service
class SaveMeeting {
    fun execute(name: String, creatorId: Long): Long {
        return 0
    }
}