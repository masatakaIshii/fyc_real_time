package fr.realtime.api.meeting.infrastructure.entrypoint

import fr.realtime.api.meeting.usecase.SaveMeeting
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.created
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/meeting")
class MeetingController(private val saveMeeting: SaveMeeting) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun createMeeting(@Valid @RequestBody request: CreateMeetingRequest): ResponseEntity<URI> {
        logger.info("Create meeting with name `${request.name}`")
        val newMeetingId = saveMeeting.execute(request.name, request.creatorId.toLong())

        val uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(newMeetingId)
                .toUri()
        return created(uri).build()
    }

}