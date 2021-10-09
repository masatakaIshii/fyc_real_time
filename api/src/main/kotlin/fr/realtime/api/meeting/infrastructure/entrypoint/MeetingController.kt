package fr.realtime.api.meeting.infrastructure.entrypoint

import fr.realtime.api.meeting.core.Meeting
import fr.realtime.api.meeting.usecase.FindAllMeetings
import fr.realtime.api.meeting.usecase.SaveMeeting
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.created
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/meeting")
class MeetingController(
        private val saveMeeting: SaveMeeting,
        private val findAllMeetings: FindAllMeetings
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun create(@Valid @RequestBody request: CreateMeetingRequest): ResponseEntity<URI> {
        logger.info("Create meeting with name `${request.name}`")
        val newMeetingId = saveMeeting.execute(request.name, request.creatorId.toLong())

        val uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(newMeetingId)
                .toUri()
        return created(uri).build()
    }

    @GetMapping
    fun findAll(): ResponseEntity<List<Meeting>> = ok(findAllMeetings.execute())
}