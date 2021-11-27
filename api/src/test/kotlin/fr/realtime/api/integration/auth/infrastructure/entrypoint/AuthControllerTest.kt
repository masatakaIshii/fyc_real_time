package fr.realtime.api.integration.auth.infrastructure.entrypoint

import com.google.gson.Gson
import fr.realtime.api.auth.infrastructure.entrypoint.RegisterRequest
import fr.realtime.api.auth.usecase.RegisterUser
import fr.realtime.api.shared.core.exceptions.ForbiddenException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.NullAndEmptySource
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

@SpringBootTest
@AutoConfigureMockMvc
internal class AuthControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var gson: Gson

    @MockBean
    lateinit var mockRegisterUser: RegisterUser

    @Nested
    inner class PostAuthRegisterTest {
        private val name = "name"

        private val email = "user@email.com"

        private val password = "password"

        @ParameterizedTest
        @NullAndEmptySource
        fun `when request name empty should send bad response`(emptyName: String?) {
            val request = emptyName?.let {
                RegisterRequest(name = emptyName, email = email, password = password)
            } ?: RegisterRequest(email = email, password = password)

            mockMvc.perform(
                post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(request))
            ).andExpect(status().isBadRequest)
        }

        @ParameterizedTest
        @NullAndEmptySource
        fun `when request email empty should send bad response`(emptyEmail: String?) {
            val request = emptyEmail?.let {
                RegisterRequest(name = name, emptyEmail, password)
            } ?: RegisterRequest(name, password = password)

            mockMvc.perform(
                post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(request))
            ).andExpect(status().isBadRequest)
        }

        @ParameterizedTest
        @NullAndEmptySource
        fun `when request password empty should send bad response`(emptyPassword: String?) {
            val request = emptyPassword?.let {
                RegisterRequest(name, "user@mail.com", emptyPassword)
            } ?: RegisterRequest(name, "user@gmail.com")

            mockMvc.perform(
                post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(request))
            ).andExpect(status().isBadRequest)
        }

        @Test
        fun `when request is correct should call usecase RegisterUser`() {
            val request = RegisterRequest(name, email, password)

            mockMvc.perform(
                post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(request))
            )

            verify(mockRegisterUser, times(1))
                .execute(name, email, password)
        }

        @Test
        fun `when usecase throw ForbiddenException should send 403 response`() {
            val request = RegisterRequest(name, email, password)
            doThrow (
                ForbiddenException("forbidden exception")
            )
                .`when`(mockRegisterUser)
                .execute(name, email, password)

            val response = mockMvc.perform(
                post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(request))
            ).andExpect(status().isForbidden)
                .andReturn()
                .response
                .contentAsString

            assertThat(response).isEqualTo("forbidden exception")
        }

        @Test
        fun `when user saved should send created with login uri response`() {
            val request = RegisterRequest(name, email, password)

            val location = mockMvc.perform(
                post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(request))
            ).andExpect(status().isCreated)
                .andReturn()
                .response
                .getHeader("Location")

            val uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/api/auth/login")
                .toUriString()
            assertThat(location).isEqualTo(uri)
        }
    }
}