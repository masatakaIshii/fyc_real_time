package fr.realtime.api.integration.theme.infrastructure

import com.google.gson.Gson
import fr.realtime.api.theme.core.Theme
import fr.realtime.api.theme.infrastructure.CreateThemeRequest
import fr.realtime.api.theme.usecase.SaveTheme
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

            val expectedTheme = Theme(name = request.name, username = request.username, meetingId = meetingId)

            verify(mockSaveTheme, times(1)).execute(expectedTheme)
        }
    }
}