package fr.realtime.api.integration.meeting.infrastructure

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.realtime.api.meeting.core.DtoMeeting
import fr.realtime.api.meeting.core.Meeting
import fr.realtime.api.meeting.infrastructure.entrypoint.CreateMeetingRequest
import fr.realtime.api.meeting.infrastructure.entrypoint.UpdateMeetingRequest
import fr.realtime.api.meeting.usecase.FindAllMeetings
import fr.realtime.api.meeting.usecase.FindMeetingThemes
import fr.realtime.api.meeting.usecase.SaveMeeting
import fr.realtime.api.meeting.usecase.UpdateMeeting
import fr.realtime.api.theme.core.Theme
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
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
internal class MeetingControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var gson: Gson

    @MockBean
    lateinit var mockSaveMeeting: SaveMeeting

    @MockBean
    lateinit var mockFindAllMeetings: FindAllMeetings

    @MockBean
    lateinit var mockFindMeetingThemes: FindMeetingThemes

    @MockBean
    lateinit var mockUpdateMeeting: UpdateMeeting

    @Nested
    inner class PostMeetingTest {

        @Test
        fun `when user not login should return unauthorized response`() {
            mockMvc.perform(
                post("/api/meeting")
            ).andExpect(status().isUnauthorized)
        }

        @WithMockUser
        @Test
        fun `when user is not admin should return forbidden response`() {
            val request = CreateMeetingRequest(name = "meeting name")
            mockMvc.perform(
                post("/api/meeting")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(request))
                    .requestAttr("userId", 654L)
            ).andExpect(status().isForbidden)
        }

        @WithMockUser(
            roles = ["USER", "ADMIN"],
        )
        @ParameterizedTest
        @NullAndEmptySource
        fun `when request name empty should send bad response`(emptyName: String?) {
            val request = emptyName?.let { CreateMeetingRequest(name = it) }
                ?: CreateMeetingRequest()
            mockMvc.perform(
                post("/api/meeting")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(request))
                    .requestAttr("userId", 654L)
            ).andExpect(status().isBadRequest)
        }

        @WithMockUser(
            roles = ["USER", "ADMIN"],
        )
        @Test
        fun `when request correct should call save meeting use case`() {
            val request = CreateMeetingRequest(name = "meeting name")
            mockMvc.perform(
                post("/api/meeting")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(request))
                    .requestAttr("userId", 654L)
            )

            verify(mockSaveMeeting, times(1)).execute(request.name, "", 654L)
        }

        @WithMockUser(
            roles = ["USER", "ADMIN"],
        )
        @Test
        fun `when new meeting saved should send new URI`() {
            val request = CreateMeetingRequest(name = "meeting name")
            `when`(mockSaveMeeting.execute(request.name, "",654L)).thenReturn(8L)


            val location = mockMvc.perform(
                post("/api/meeting").contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(request))
                    .requestAttr("userId", 654L)
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
        fun `user should be authenticated`() {
            mockMvc.perform(
                get("/api/meeting")
            ).andExpect(status().isUnauthorized)
        }

        @WithMockUser
        @Test
        fun `should call use case to find all meetings`() {
            mockMvc.perform(
                get("/api/meeting")
            )

            verify(mockFindAllMeetings, times(1)).execute()
        }

        @WithMockUser
        @Test
        fun `when use case return list meetings should return response all meetings`() {
            val localDateTime = LocalDateTime.now()
            val listDtoMeetings = listOf(
                DtoMeeting(
                    id = 1,
                    name = "first meeting",
                    description = "description first meeting",
                    createdDateTime = localDateTime,
                    isClosed = false
                ),
                DtoMeeting(
                    id = 2,
                    name = "second meeting",
                    description = "description second meeting",
                    createdDateTime = localDateTime,
                    isClosed = true
                )
            )
            `when`(mockFindAllMeetings.execute()).thenReturn(listDtoMeetings)

            val contentAsString = mockMvc.perform(
                get("/api/meeting")
            ).andExpect(status().isOk)
                .andReturn()
                .response
                .contentAsString

            val itemType = object : TypeToken<List<DtoMeeting>>() {}.type
            val result: List<DtoMeeting> = gson.fromJson(contentAsString, itemType)
            assertThat(result).isEqualTo(listDtoMeetings)
        }
    }

    @Nested
    inner class FindMeetingThemesTest {
        private val meetingId = 6854L

        @Test
        fun `when user not authenticate shoudl return unauthorized response`() {
            mockMvc.perform(get("/api/meeting/1/theme"))
                .andExpect(status().isUnauthorized)
        }

        @WithMockUser
        @ParameterizedTest
        @ValueSource(strings = ["notnum", "-1", "0", "1.2"])
        fun `when meetingId is not correct should send bad request response`(notCorrectMeetingId: String) {
            mockMvc.perform(get("/api/meeting/$notCorrectMeetingId/theme"))
                .andExpect(status().isBadRequest)
        }

        @WithMockUser
        @Test
        fun `when meetingId is correct should call use case FindMeetingThemes`() {
            mockMvc.perform(get("/api/meeting/$meetingId/theme"))

            verify(mockFindMeetingThemes, times(1)).execute(meetingId)
        }

        @WithMockUser
        @Test
        fun `when use case FindMeetingThemes return list of themes should return list`() {
            val listThemes = listOf(
                Theme(354, "theme354", "username354", meetingId),
                Theme(2538, "theme2538", "username2538", meetingId)
            )
            `when`(mockFindMeetingThemes.execute(meetingId)).thenReturn(listThemes)

            val contentAsString = mockMvc.perform(get("/api/meeting/$meetingId/theme"))
                .andExpect(status().isOk)
                .andReturn()
                .response
                .contentAsString

            val itemType = object : TypeToken<List<Theme>>() {}.type
            val result: List<Theme> = gson.fromJson(contentAsString, itemType)
            assertThat(result).isEqualTo(listThemes)
        }
    }

    @Nested
    inner class UpdateMeetingTest {
        private val meetingId = 3658L

        @Test
        fun `when user is not authenticated should send unauthorized response`() {
            mockMvc.perform(put("/api/meeting/$meetingId"))
                .andExpect(status().isUnauthorized)
        }

        @WithMockUser
        @Test
        fun `when user is not admin should send forbidden response`() {
            val request = UpdateMeetingRequest("new meeting name", UUID.randomUUID(), true)
            mockMvc.perform(
                put("/api/meeting/$meetingId")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(request))
            )
                .andExpect(status().isForbidden)
        }

        @WithMockUser(roles = ["USER", "ADMIN"])
        @Test
        fun `when request not send should return bad request`() {
            mockMvc.perform(put("/api/meeting/$meetingId"))
                .andExpect(status().isBadRequest)
        }

        @WithMockUser(roles = ["USER", "ADMIN"])
        @ParameterizedTest
        @ValueSource(strings = ["notnum", "-1", "0", "1.2"])
        fun `when meetingId is not correct should send bad request response`(notCorrectMeetingId: String) {
            val request = UpdateMeetingRequest("new meeting name", UUID.randomUUID(), true)
            mockMvc.perform(
                put("/api/meeting/$notCorrectMeetingId")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(request))
            )
                .andExpect(status().isBadRequest)
        }

        @WithMockUser(roles = ["USER", "ADMIN"])
        @Test
        fun `when meetingId is correct should call UpdateMeeting usecase`() {
            val request = UpdateMeetingRequest("new meeting name", UUID.randomUUID(), true)
            mockMvc.perform(
                put("/api/meeting/$meetingId").contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(request))
            )

            verify(mockUpdateMeeting, times(1))
                .execute(meetingId, request.name, request.uuid, request.isClosed)
        }

        @WithMockUser(roles = ["USER", "ADMIN"])
        @Test
        fun `when request contain only name should call UpdateMeeting usecase with name and others param null`() {
            val request = UpdateMeetingRequest("new meeting name")
            mockMvc.perform(
                put("/api/meeting/$meetingId").contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(request))
            )

            verify(mockUpdateMeeting, times(1))
                .execute(meetingId, request.name, null, null)
        }

        @WithMockUser(roles = ["USER", "ADMIN"])
        @Test
        fun `when request contain only uuid should call UpdateMeeting usecase with uuid and others param null`() {
            val uuid = UUID.randomUUID()
            val request = UpdateMeetingRequest(uuid = uuid)

            mockMvc.perform(
                put("/api/meeting/$meetingId").contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(request))
            )

            verify(mockUpdateMeeting, times(1))
                .execute(meetingId, null, uuid, null)
        }

        @WithMockUser(roles = ["USER", "ADMIN"])
        @Test
        fun `when request contain only isClosed should call UpdateMeeting usecase with isClosed and others param null`() {
            val request = UpdateMeetingRequest(isClosed = false)

            mockMvc.perform(
                put("/api/meeting/$meetingId").contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(request))
            )

            verify(mockUpdateMeeting, times(1))
                .execute(meetingId, null, null, false)
        }

        @WithMockUser(roles = ["USER", "ADMIN"])
        @Test
        fun `when request success should return ok response`() {
            val request = UpdateMeetingRequest("new meeting name", UUID.randomUUID(), true)
            mockMvc.perform(
                put("/api/meeting/$meetingId").contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(request))
            ).andExpect(status().isNoContent)
        }
    }
}