package fr.realtime.api.unit.auth.usecase

import fr.realtime.api.auth.usecase.RegisterUser
import fr.realtime.api.shared.core.exceptions.ForbiddenException
import fr.realtime.api.shared.core.utils.PasswordUtils
import fr.realtime.api.user.core.*
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
    lateinit var mockRoleDao: RoleDao

    @Mock
    lateinit var mockPasswordUtils: PasswordUtils

    @BeforeEach
    fun setup() {
        registerUser = RegisterUser(mockUserDao, mockRoleDao, mockPasswordUtils)
    }

    private val name = "name"

    private val email = "user@mail.com"

    private val password = "password"

    @Test
    fun `when user found by email should throw exception`() {
        `when`(mockUserDao.existsByEmail(email)).thenReturn(true)

        assertThatThrownBy{ registerUser.execute(name, email, password) }
            .isExactlyInstanceOf(ForbiddenException::class.java)
            .hasMessage("USER_EMAIL_ALREADY_EXIST : User with email '$email' already exists")
    }

    @Test
    fun `when user role not found by role dao should throw exception`() {
        `when`(mockUserDao.existsByEmail(email)).thenReturn(false)
        `when`(mockRoleDao.findByName(RoleName.ROLE_USER)).thenReturn(null)

        assertThatThrownBy{ registerUser.execute(name, email, password) }
            .isExactlyInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("ROLE_USER_NOT_EXIST : Can't register user without role")
    }

    @Test
    fun `when user not found should encode password`() {
        `when`(mockUserDao.existsByEmail(email)).thenReturn(false)
        `when`(mockRoleDao.findByName(RoleName.ROLE_USER)).thenReturn(Role(1, RoleName.ROLE_USER))

        registerUser.execute(name, email, password)

        verify(mockPasswordUtils, times(1)).encode(password)
    }

    @Test
    fun `when password encoded should save new user with encoded password`() {
        `when`(mockUserDao.existsByEmail(email)).thenReturn(false)
        val role = Role(1, RoleName.ROLE_USER)
        `when`(mockRoleDao.findByName(RoleName.ROLE_USER)).thenReturn(role)
        val encodedPassword = "encoded password"
        `when`(mockPasswordUtils.encode(password))
            .thenReturn(encodedPassword)

        registerUser.execute(name, email, password)

        val userToSave = User(
            id = 0,
            name = name,
            email = email,
            password = encodedPassword,
            roles = setOf(role)
        )
        verify(mockUserDao, times(1)).save(userToSave)
    }
}