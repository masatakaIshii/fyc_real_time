package fr.realtime.api.auth.usecase

import fr.realtime.api.shared.core.exceptions.ForbiddenException
import fr.realtime.api.shared.core.exceptions.NotFoundException
import fr.realtime.api.shared.core.utils.PasswordUtils
import fr.realtime.api.user.core.*
import org.hibernate.exception.DataException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class RegisterUser(
    private val userDao: UserDao,
    private val roleDao: RoleDao,
    private val passwordUtils: PasswordUtils
    ) {

    @Throws(ForbiddenException::class)
    fun execute(name: String, email: String, password: String) {
        if (userDao.existsByEmail(email)) {
            throw ForbiddenException(
                "USER_EMAIL_ALREADY_EXIST : User with email '$email' already exists"
            )
        }
        val userRole = roleDao.findByName(RoleName.ROLE_USER) ?: throw IllegalArgumentException(
            "ROLE_USER_NOT_EXIST : Can't register user without role"
        )
        val encodedPassword = passwordUtils.encode(password)

        userDao.save(User(0, name, encodedPassword, email, setOf(userRole)))
    }
}