package fr.realtime.api.unit.auth.usecase

import fr.realtime.api.auth.usecase.RegisterUser
import fr.realtime.api.shared.core.exceptions.ForbiddenException
import fr.realtime.api.shared.core.utils.PasswordUtils
import fr.realtime.api.user.core.User
import fr.realtime.api.user.core.UserDao
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class RegisterUserTest {
    lateinit var registerUser: RegisterUser

    @Mock
    lateinit var mockUserDao: UserDao

    @Mock
    lateinit var mockPasswordUtils: PasswordUtils

    @BeforeEach
    fun setup() {
        registerUser = RegisterUser(mockUserDao, mockPasswordUtils)
    }

    private val name = "name"

    private val email = "user@mail.com"

    private val password = "password"

    @Test
    fun `should call find user by email`() {
        registerUser.execute(name, email, password)

        verify(mockUserDao, times(1)).existsByEmail(email)
    }

    @Test
    fun `when user found by email should throw exception`() {
        `when`(mockUserDao.existsByEmail(email)).thenReturn(true)

        assertThatThrownBy{ registerUser.execute(name, email, password) }
            .isExactlyInstanceOf(ForbiddenException::class.java)
            .hasMessage("USER_EMAIL_ALREADY_EXIST : User with email '$email' already exists")
    }

    @Test
    fun `when user not found should encode password`() {
        `when`(mockUserDao.existsByEmail(email)).thenReturn(false)

        registerUser.execute(name, email, password)

        verify(mockPasswordUtils, times(1)).hash(password, PasswordUtils.CURRENT_SALT.toByteArray())
    }

    @Test
    fun `when password encoded should save new user with encoded password`() {
        `when`(mockUserDao.existsByEmail(email)).thenReturn(false)
        val encodedPassword = "encoded password"
        `when`(mockPasswordUtils.hash(password, PasswordUtils.CURRENT_SALT.toByteArray()))
            .thenReturn(encodedPassword)

        registerUser.execute(name, email, password)

        val userToSave = User(
            id = 0,
            name = name,
            email = email,
            password = encodedPassword
        )
        verify(mockUserDao, times(1)).save(userToSave)
    }
}