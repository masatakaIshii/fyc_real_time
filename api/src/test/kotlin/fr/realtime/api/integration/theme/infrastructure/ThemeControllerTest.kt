package fr.realtime.api.integration.theme.infrastructure

import com.google.gson.Gson
import fr.realtime.api.theme.infrastructure.entrypoint.CreateThemeRequest
import fr.realtime.api.theme.infrastructure.usecase.SaveTheme
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@AutoConfigureMockMvc
@SpringBootTest
internal class ThemeControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var gson: Gson

    @MockBean
    lateinit var mockSaveTheme: SaveTheme

    @Nested
    inner class PostThemeTest {
        private val meetingId = 594L

        @ParameterizedTest
        @NullAndEmptySource
        fun `when request name empty should send bad response`(emptyName: String?) {
            val request = emptyName?.let { CreateThemeRequest(name = it, username = "user name", meetingId = meetingId.toString()) }
                    ?: CreateThemeRequest(username = "user name", meetingId = meetingId.toString())
            val contentAsString = mockMvc.perform(
                    post("/api/theme")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(gson.toJson(request))
            )
                    .andExpect(status().isBadRequest)
                    .andReturn()
                    .response
                    .contentAsString
            assertThat(contentAsString).isEqualTo("{\"name\":\"Theme name cannot be empty\"}")
        }

        @ParameterizedTest
        @NullAndEmptySource
        fun `when request username empty should send bad response`(emptyUserName: String?) {
            val request = emptyUserName?.let { CreateThemeRequest(name = "new theme", username = it, meetingId = meetingId.toString()) }
                    ?: CreateThemeRequest(name = "new theme", meetingId = meetingId.toString())
            val contentAsString = mockMvc.perform(
                    post("/api/theme")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(gson.toJson(request))
            )
                    .andExpect(status().isBadRequest)
                    .andReturn()
                    .response
                    .contentAsString
            assertThat(contentAsString).isEqualTo("{\"username\":\"Theme username cannot be empty\"}")
        }

        @ParameterizedTest
        @ValueSource(strings = ["-999", "-1", "0", "notnumber"])
        fun `when request meetingId not unsigned integer should send bad request`(notCorrectMeetingId: String) {
            val request = CreateThemeRequest(name = "new theme", username = "user name", meetingId = notCorrectMeetingId)
            mockMvc.perform(
                    post("/api/theme")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(gson.toJson(request))
            )
                    .andExpect(status().isBadRequest)
        }

        @Test
        fun `when request correct should call save theme`() {
            val request = CreateThemeRequest(name = "new theme", username = "user name", meetingId = meetingId.toString())
            mockMvc.perform(
                    post("/api/theme")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(gson.toJson(request))
            )

            verify(mockSaveTheme, times(1)).execute(
                    request.name,
                    request.username,
                    request.meetingId.toLong()
            )
        }

        private val newThemeId = 2583L

        @Test
        fun `when theme saved and use case return new theme id should return created response`() {
            val request = CreateThemeRequest(name = "new theme", username = "user name", meetingId = meetingId.toString())
            `when`(mockSaveTheme.execute(request.name, request.username, request.meetingId.toLong())).thenReturn(newThemeId)

            val location = mockMvc.perform(
                    post("/api/theme")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(gson.toJson(request))
            ).andExpect(status().isCreated)
                    .andReturn()
                    .response
                    .getHeader("Location")

            val expectedUri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                    .path("/api/theme/{id}")
                    .buildAndExpand(2583L)
                    .toUriString()
            assertThat(location).isEqualTo(expectedUri)
        }
    }
}