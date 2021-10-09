package fr.realtime.api.user.infrastructure.bootstrap

import fr.realtime.api.shared.core.utils.PasswordUtils
import fr.realtime.api.user.core.User
import fr.realtime.api.user.core.UserDao
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.nio.charset.Charset

@Component
class UserBootstrap(private val userDao: UserDao, private val passwordUtils: PasswordUtils) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @EventListener
    fun on(event: ApplicationReadyEvent) {
        val password = "root"

        logger.info("Create initial user 'root' with password '$password'")
        val passwordEncoded = passwordUtils.hash(password, PasswordUtils.CURRENT_SALT.toByteArray())

        val initialUser = User(
                0, name = "root", password = passwordEncoded
        )

        val savedUser = userDao.save(initialUser)

        logger.info("User '${savedUser.name}' created")
    }
}