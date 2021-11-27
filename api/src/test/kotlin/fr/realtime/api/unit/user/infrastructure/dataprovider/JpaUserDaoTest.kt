package fr.realtime.api.unit.user.infrastructure.dataprovider

import fr.realtime.api.user.core.RoleName
import fr.realtime.api.user.core.User
import fr.realtime.api.user.infrastructure.dataprovider.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class JpaUserDaoTest {
    lateinit var jpaUserDao: JpaUserDao

    @Mock
    lateinit var mockUserRepository: UserRepository

    val userMapper: UserMapper = UserMapper()

    @BeforeEach
    fun setup() {
        jpaUserDao = JpaUserDao(mockUserRepository, userMapper)
    }

    @Nested
    inner class FindByIdTest {
        private val userId = 3654L

        @Test
        fun `should call user repository findOneById`() {
            jpaUserDao.findById(userId)

            verify(mockUserRepository, times(1)).findById(userId)
        }

        @Test
        fun `when user found should return founded user`() {
            val foundUser = JpaUser(
                userId,
                "found user",
                "password",
                "found@user.com",
                setOf(JpaRole(1, RoleName.ROLE_USER))
            )
            `when`(mockUserRepository.findById(userId)).thenReturn(Optional.of(foundUser))

            val result = jpaUserDao.findById(userId)

            val expectedUser = userMapper.entityToDomain(foundUser)
            assertThat(result).isEqualTo(expectedUser)
        }
    }

    @Nested
    inner class SaveTest {
        @Test
        fun `when user saved should return saved user`() {
            val userToSave = User(id = 0, name = "user name", password = "password", "user@name.com")
            val entityUserToSave = userMapper.domainToEntity(userToSave)

            val entitySavedUser = entityUserToSave.copy(id = 3584L)
            `when`(mockUserRepository.save(entityUserToSave)).thenReturn(entitySavedUser)


            val result = jpaUserDao.save(userToSave)

            val expected = userMapper.entityToDomain(entitySavedUser)
            assertThat(result).isEqualTo(expected)
        }
    }

    @Nested
    inner class FindByEmail {
        private val email = "user@mail.com"

        @Test
        fun `should call userRepository to find user by email`() {
            jpaUserDao.findByEmail(email)

            verify(mockUserRepository, times(1)).findByEmail(email)
        }

        @Test
        fun `when user not found by email should return null`() {
            `when`(mockUserRepository.findByEmail(email)).thenReturn(Optional.empty())

            val result = jpaUserDao.findByEmail(email)

            assertThat(result).isNull()
        }

        @Test
        fun `when user found should return found user`() {
            val foundUser = JpaUser(
                id = 354L,
                name = "name",
                email = email,
                password = "password",
                roles = setOf(JpaRole(1, RoleName.ROLE_USER))
            )
            `when`(mockUserRepository.findByEmail(email)).thenReturn(Optional.of(foundUser))

            val result = jpaUserDao.findByEmail(email)

            val expectedUser = User(
                id = foundUser.id,
                name = foundUser.name,
                email = foundUser.email,
                password = foundUser.password
            )
            assertThat(result).isEqualTo(expectedUser)
        }
    }
}