package fr.realtime.api.meeting.infrastructure.entrypoint

import fr.realtime.api.meeting.core.Meeting
import fr.realtime.api.meeting.usecase.FindAllMeetings
import fr.realtime.api.meeting.usecase.FindMeetingThemes
import fr.realtime.api.meeting.usecase.SaveMeeting
import fr.realtime.api.meeting.usecase.UpdateMeeting
import fr.realtime.api.theme.core.Theme
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import javax.validation.Valid
import javax.validation.constraints.Min
import javax.validation.constraints.Pattern

@RestController
@RequestMapping("/api/meeting")
@Validated
class MeetingController(
        private val saveMeeting: SaveMeeting,
        private val findAllMeetings: FindAllMeetings,
        private val findMeetingThemes: FindMeetingThemes,
        private val updateMeeting: UpdateMeeting
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

    @GetMapping("{meetingId}/theme")
    fun findMeetingThemes(
            @Min(1)
            @Pattern(regexp = "[0-9]+", message = "Meeting id has to be integer")
            @PathVariable meetingId: String
    ): ResponseEntity<List<Theme>> {
        val result = findMeetingThemes.execute(meetingId.toLong())
        return ok(result)
    }

    @PutMapping("{meetingId}")
    fun updateMeeting(
            @Min(1)
            @Pattern(regexp = "[0-9]+", message = "Meeting id has to be integer")
            @PathVariable meetingId: String,
            @RequestBody request: UpdateMeetingRequest
    ): ResponseEntity<Void> {
        logger.info("Update meeting with id $meetingId, request body : $request")
        updateMeeting.execute(meetingId.toLong(), request.name, request.uuid, request.isClosed)
        return noContent().build()
    }
}