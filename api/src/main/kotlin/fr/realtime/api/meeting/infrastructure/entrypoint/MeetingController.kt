package fr.realtime.api.meeting.infrastructure.entrypoint

import fr.realtime.api.meeting.core.DtoMeeting
import fr.realtime.api.meeting.core.Meeting
import fr.realtime.api.meeting.usecase.*
import fr.realtime.api.theme.core.Theme
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import javax.validation.Valid
import javax.validation.constraints.Min
import javax.validation.constraints.Pattern

@CrossOrigin(origins = ["http://localhost:5000/"], maxAge = 3600)
@RestController
@RequestMapping("/api/meeting")
@Validated
class MeetingController(
        private val saveMeeting: SaveMeeting,
        private val findAllMeetings: FindAllMeetings,
        private val findMeetingThemes: FindMeetingThemes,
        private val updateMeeting: UpdateMeeting,
        private val deleteMeetingById: DeleteMeetingById
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun create(
        @Valid @RequestBody request: CreateMeetingRequest,
        @RequestAttribute("userId")
        @Min(value = 1, message = "id has to be equal or more than 1") userId: Long,
    ): ResponseEntity<URI> {
        logger.info("Create meeting with name `${request.name}`")
        val newMeetingId = saveMeeting.execute(request.name, request.description ,userId)

        val uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(newMeetingId)
                .toUri()
        return created(uri).build()
    }

    @GetMapping
    fun findAll(): ResponseEntity<List<DtoMeeting>> = ok(findAllMeetings.execute())

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
    @PreAuthorize("hasRole('ADMIN')")
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

    @DeleteMapping("{meetingId}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteMeeting(
        @Min(1)
        @Pattern(regexp = "[0-9]+", message = "Meeting id has to be integer")
        @PathVariable meetingId: String,
        @RequestAttribute("userId")
        @Min(value = 1, message = "id has to be equal or more than 1") userId: Long,
    ): ResponseEntity<Void> {
        logger.info("Delete meeting with id $meetingId")

        deleteMeetingById.execute(meetingId.toLong(), userId)

        return noContent().build()
    }
}