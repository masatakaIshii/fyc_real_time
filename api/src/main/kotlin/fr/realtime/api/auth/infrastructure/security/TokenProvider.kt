package fr.realtime.api.auth.infrastructure.security

import io.jsonwebtoken.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException
import java.time.Duration
import java.util.*
import java.util.stream.Collectors

@Component
class TokenProvider {
    private val logger = LoggerFactory.getLogger(this::class.java)
    companion object {
        private const val AUTHORITIES_KEY = "auth"
        private val TOKEN_VALIDITY_IN_MILLISECONDS = Duration.ofDays(1).seconds * 1000
    }
    @get:Value("\${security.token.secret}")
    val secretToken: String? = null

    private val secret: ByteArray
        get() {
            return secretToken.toString().toByteArray()
        }

    fun createToken(authentication: Authentication): String {
        val authorities = authentication.authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","))
        val now = Date().time
        val validity = Date(now + TOKEN_VALIDITY_IN_MILLISECONDS)

        return Jwts.builder()
            .setSubject(authentication.name)
            .claim(AUTHORITIES_KEY, authorities)
            .signWith(SignatureAlgorithm.HS512, secret)
            .setExpiration(validity)
            .compact()
    }

    private fun parseToken(authToken: String): Jws<Claims> {
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(authToken)
    }

    fun validateToken(authToken: String): Boolean {
        return try {
            parseToken(authToken)
            true
        } catch (ex: JwtException) {
            logger.info("Invalid JWT token.")
            false
        } catch (ex: IllegalArgumentException) {
            logger.info("Invalid JWT token.")
            false
        }
    }

    fun getAuthentication(token: String): Authentication {
        val claims = parseToken(token).body

        val authorities = claims[AUTHORITIES_KEY].toString()
            .split(",")
            .map(::SimpleGrantedAuthority)
        val principal = User(claims.subject, "", authorities)
        return UsernamePasswordAuthenticationToken(principal, token, authorities)
    }
}