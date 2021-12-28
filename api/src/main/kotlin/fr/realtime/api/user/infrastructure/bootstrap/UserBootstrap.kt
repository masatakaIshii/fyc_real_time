package fr.realtime.api.user.infrastructure.bootstrap

import fr.realtime.api.shared.core.utils.PasswordUtils
import fr.realtime.api.user.core.*
import org.slf4j.LoggerFactory

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class UserBootstrap(
    private val roleDao: RoleDao,
    private val userDao: UserDao,
    private val passwordUtils: PasswordUtils
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @EventListener
    fun on(event: ApplicationReadyEvent) {
        val password = "root"

        logger.info("Create initial user 'root' with password {}", password)
        val passwordEncoded = passwordUtils.encode(password)

        val userRole = if (!roleDao.existsByName(RoleName.ROLE_USER)) {
            roleDao.save(Role(0, RoleName.ROLE_USER))
        } else roleDao.findByName(RoleName.ROLE_USER) ?: throw Exception("Problem User bootstrap")

        val adminRole = if (!roleDao.existsByName(RoleName.ROLE_ADMIN)) {
            roleDao.save(Role(0, RoleName.ROLE_ADMIN))
        } else roleDao.findByName(RoleName.ROLE_ADMIN) ?: throw Exception("Problem User bootstrap")

        val initialUser = User(
            0, name = "root",
            password = passwordEncoded,
            "root@root.com",
            roles = setOf(userRole, adminRole)
        )

        val savedUser = userDao.save(initialUser)

        logger.info("User '${savedUser.name}' created")
    }
}