package fr.realtime.api.auth.infrastructure.security.login

import fr.realtime.api.auth.infrastructure.security.TokenProvider
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils.hasText
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

class JwtAuthorizationFilter(private val tokenProvider: TokenProvider) : GenericFilterBean(){
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val token = resolveToken(request as HttpServletRequest)
        if (token != null && hasText(token) &&  tokenProvider.validateToken(token)) {
            val authentication = tokenProvider.getAuthentication(token)
            SecurityContextHolder.getContext().authentication = authentication
        }

        chain?.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerPart = "Bearer ";
        val authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (hasText(authorization) && authorization.startsWith(bearerPart)) {
            return authorization.substring(bearerPart.length);
        }

        return null;
    }
}