package fr.realtime.api.integration.meeting.infrastructure

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.realtime.api.meeting.core.Meeting
import fr.realtime.api.meeting.infrastructure.entrypoint.CreateMeetingRequest
import fr.realtime.api.meeting.usecase.FindAllMeetings
import fr.realtime.api.meeting.usecase.SaveMeeting
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.time.LocalDateTime

@SpringBootTest
@AutoConfigureMockMvc
internal class MeetingControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc;

    @Autowired
    lateinit var gson: Gson;

    @MockBean
    lateinit var mockSaveMeeting: SaveMeeting

    @MockBean
    lateinit var mockFindAllMeetings: FindAllMeetings

    @Nested
    inner class PostMeetingTest {
        @ParameterizedTest
        @NullAndEmptySource
        fun `when request name empty should send bad response`(emptyName: String?) {
            val request = emptyName?.let { CreateMeetingRequest(name = it, creatorId = "654") }
                    ?: CreateMeetingRequest(creatorId = "654")
            mockMvc.perform(
                    post("/api/meeting").contentType(MediaType.APPLICATION_JSON)
                            .content(gson.toJson(request))
            ).andExpect(status().isBadRequest)
        }

        @ParameterizedTest
        @NullAndEmptySource
        fun `when request creator id empty should send bad response`(emptyCreatorId: String?) {
            val request = emptyCreatorId?.let { CreateMeetingRequest(name = "meeting name", creatorId = emptyCreatorId) }
                    ?: CreateMeetingRequest(name = "meeting name")
            mockMvc.perform(
                    post("/api/meeting").contentType(MediaType.APPLICATION_JSON)
                            .content(gson.toJson(request))
            ).andExpect(status().isBadRequest);
        }

        @ParameterizedTest
        @ValueSource(strings = ["notnum", "-99", "-1", "0", "1.5"])
        fun `when request creator id not integer min 1 should send bad response`(badCreatorId: String) {
            val request = CreateMeetingRequest(name = "meeting name", creatorId = badCreatorId)
            mockMvc.perform(
                    post("/api/meeting").contentType(MediaType.APPLICATION_JSON)
                            .content(gson.toJson(request))
            ).andExpect(status().isBadRequest)
        }

        @Test
        fun `when request correct should call save meeting use case`() {
            val request = CreateMeetingRequest(name = "meeting name", creatorId = "654")
            mockMvc.perform(
                    post("/api/meeting").contentType(MediaType.APPLICATION_JSON)
                            .content(gson.toJson(request))
            );

            verify(mockSaveMeeting, times(1)).execute(request.name, 654L)
        }

        @Test
        fun `when new meeting saved should send new URI`() {
            val request = CreateMeetingRequest(name = "meeting name", creatorId = "654")
            `when`(mockSaveMeeting.execute(request.name, 654L)).thenReturn(8L)


            val location = mockMvc.perform(
                    post("/api/meeting").contentType(MediaType.APPLICATION_JSON)
                            .content(gson.toJson(request))
            ).andExpect(status().isCreated)
                    .andReturn()
                    .response
                    .getHeader("Location")

            val uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                    .path("/api/meeting/{id}")
                    .buildAndExpand(8L)
                    .toUriString()

            assertThat(location).isEqualTo(uri)
        }
    }

    @Nested
    inner class FindAllTest {
        @Test
        fun `should call use case to find all meetings`() {
            mockMvc.perform(
                    get("/api/meeting")
            )

            verify(mockFindAllMeetings, times(1)).execute()
        }

        @Test
        fun `when use case return list meetings should return response all meetings`() {
            val listMeetings = listOf(
                    Meeting(id = 1, name = "first meeting", createdDateTime = LocalDateTime.now(), creatorId = 354, isClosed = false),
                    Meeting(id = 2, name = "second meeting", createdDateTime = LocalDateTime.now(), creatorId = 658, isClosed = true)
            )
            `when`(mockFindAllMeetings.execute()).thenReturn(listMeetings)

            val contentAsString = mockMvc.perform(
                    get("/api/meeting")
            ).andExpect(status().isOk)
                    .andReturn()
                    .response
                    .contentAsString

            val itemType = object : TypeToken<List<Meeting>>(){}.type
            val result: List<Meeting> = gson.fromJson(contentAsString, itemType)
            assertThat(result).isEqualTo(listMeetings)
        }
    }
}