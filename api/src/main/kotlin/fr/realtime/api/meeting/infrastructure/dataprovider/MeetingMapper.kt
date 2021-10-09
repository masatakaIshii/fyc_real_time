package fr.realtime.api.meeting.infrastructure.dataprovider

import fr.realtime.api.meeting.core.Meeting
import fr.realtime.api.shared.core.mapper.Mapper
import org.springframework.stereotype.Component

@Component
class MeetingMapper : Mapper<Meeting, JpaMeeting> {
    override fun entityToDomain(entity: JpaMeeting): Meeting {
        return Meeting(
                entity.id,
                entity.name,
                entity.uuid,
                entity.createdDateTime,
                entity.creatorId
        )
    }

    override fun domainToEntity(domain: Meeting): JpaMeeting {
        return JpaMeeting(
                domain.id,
                domain.name,
                domain.uuid,
                domain.createdDateTime,
                domain.creatorId
        )
    }
}