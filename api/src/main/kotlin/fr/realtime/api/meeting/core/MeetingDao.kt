package fr.realtime.api.meeting.core

interface MeetingDao {
    fun save(meeting: Meeting): Meeting
    fun findAll(): List<Meeting>
    fun findById(meetingId: Long): Meeting?
}