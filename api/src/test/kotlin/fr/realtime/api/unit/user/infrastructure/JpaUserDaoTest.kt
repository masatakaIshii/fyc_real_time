package fr.realtime.api.unit.user.infrastructure

import fr.realtime.api.user.core.User
import fr.realtime.api.user.infrastructure.dataprovider.JpaUser
import fr.realtime.api.user.infrastructure.dataprovider.JpaUserDao
import fr.realtime.api.user.infrastructure.dataprovider.UserMapper
import fr.realtime.api.user.infrastructure.dataprovider.UserRepository
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
            val foundUser = JpaUser(userId, "found user", "password")
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
            val userToSave = User(id = 0, name = "user name", password = "password")
            val entityUserToSave = userMapper.domainToEntity(userToSave)

            val entitySavedUser = entityUserToSave.copy(id = 3584L)
            `when`(mockUserRepository.save(entityUserToSave)).thenReturn(entitySavedUser)


            val result = jpaUserDao.save(userToSave)

            val expected = userMapper.entityToDomain(entitySavedUser)
            assertThat(result).isEqualTo(expected)
        }

    }
}