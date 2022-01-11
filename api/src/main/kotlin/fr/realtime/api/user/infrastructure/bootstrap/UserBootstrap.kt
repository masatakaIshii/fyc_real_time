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

        val userRole = if (!roleDao.existsByName(RoleName.ROLE_USER)) {
            roleDao.save(Role(0, RoleName.ROLE_USER))
        } else roleDao.findByName(RoleName.ROLE_USER) ?: throw Exception("Problem User bootstrap")

        val adminRole = if (!roleDao.existsByName(RoleName.ROLE_ADMIN)) {
            roleDao.save(Role(0, RoleName.ROLE_ADMIN))
        } else roleDao.findByName(RoleName.ROLE_ADMIN) ?: throw Exception("Problem User bootstrap")

        createUser("root", "root@root.com", "root", setOf(userRole, adminRole))
        createUser("admin", "admin@admin.com", "admin", setOf(userRole, adminRole))
    }

    fun createUser(name: String, email: String, password: String, roles: Set<Role>) {
        logger.info("Create initial user 'root' with password {}", password)
        val passwordEncoded = passwordUtils.encode(password)

        val userToSave = User(0, name, passwordEncoded, email, roles)

        val savedUser = userDao.save(userToSave)

        logger.info("User '${savedUser.name}' created")
    }
}