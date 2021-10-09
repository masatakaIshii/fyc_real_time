package fr.realtime.api.meeting.infrastructure.dataprovider

import org.springframework.data.jpa.repository.JpaRepository

interface MeetingRepository : JpaRepository<JpaMeeting, Long> {
}