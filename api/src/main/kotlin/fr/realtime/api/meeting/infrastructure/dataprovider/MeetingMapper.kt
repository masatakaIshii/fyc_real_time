package fr.realtime.api.meeting.infrastructure.dataprovider

import fr.realtime.api.meeting.core.Meeting
import fr.realtime.api.shared.core.mapper.Mapper
import org.springframework.stereotype.Component

@Component
class MeetingMapper : Mapper<Meeting, JpaMeeting> {

    override fun entityToDomain(entity: JpaMeeting): Meeting = Meeting(
        entity.id,
        entity.name,
        entity.description,
        entity.uuid,
        entity.createdDateTime,
        entity.creatorId,
        entity.isClosed
    )

    override fun domainToEntity(domain: Meeting): JpaMeeting = JpaMeeting(
        domain.id,
        domain.name,
        domain.description,
        domain.uuid,
        domain.createdDateTime,
        domain.creatorId,
        domain.isClosed
    )
}