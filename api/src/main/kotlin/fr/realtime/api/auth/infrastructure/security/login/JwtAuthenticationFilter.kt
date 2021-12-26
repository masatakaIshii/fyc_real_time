package fr.realtime.api.auth.infrastructure.security.login


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import fr.realtime.api.auth.infrastructure.security.TokenProvider
import fr.realtime.api.shared.core.utils.PasswordUtils
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter(
    authenticationManager: AuthenticationManager?,
    private val passwordUtils: PasswordUtils,
    private val tokenProvider: TokenProvider
) :
    UsernamePasswordAuthenticationFilter(authenticationManager) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private var mapper: ObjectMapper? = null;

    init {
        super.setFilterProcessesUrl("/api/login")
        mapper = jacksonObjectMapper()
    }

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication? {
        try {
            val loginRequest = mapper?.readValue(request?.inputStream, LoginRequest::class.java)
            logger.info("loginRequest : $loginRequest")
            if (loginRequest != null) {
                return authenticationManager.authenticate(UsernamePasswordAuthenticationToken(
                    loginRequest.email,
                    passwordUtils.hash(loginRequest.password, PasswordUtils.CURRENT_SALT.toByteArray())
                ))
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            logger.error("error authentication : ${ex.message}")
            throw BadCredentialsException("Could not authenticate user")
        }
        return null
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        logger.info("successful authentication")
        val token = authResult?.let { tokenProvider.createToken(it) }
        response?.addHeader(HttpHeaders.AUTHORIZATION, "Bearer $token")
        response?.writer?.flush()
    }
}