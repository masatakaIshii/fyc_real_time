package fr.realtime.api.auth.infrastructure.security

import fr.realtime.api.shared.core.exceptions.ForbiddenException
import fr.realtime.api.user.core.UserDao
import org.slf4j.LoggerFactory
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl (private val userDao: UserDao) : UserDetailsService {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun loadUserByUsername(username: String?): UserDetails {
        if (username.isNullOrEmpty()) {
            logger.error("user email empty")
            throw ForbiddenException("USER_EMAIL_EMPTY")
        }
        logger.info("user to load by email $username")

        val user = userDao.findByEmail(username)
            ?: throw UsernameNotFoundException("USER_EMAIL_NOT_FOUND: user with email $username not found")
        val authorities = mutableListOf<SimpleGrantedAuthority>()
        val userRoles = userDao.findRolesByUserId(user.id)

        for (role in userRoles) {
            logger.info(role.toString())
            authorities.add(SimpleGrantedAuthority(role.name.toString()))
        }

        return org.springframework.security.core.userdetails.User(
            user.email,
            user.password,
            authorities
        )
    }
}