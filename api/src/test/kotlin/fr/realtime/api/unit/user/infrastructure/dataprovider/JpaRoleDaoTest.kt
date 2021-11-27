package fr.realtime.api.unit.user.infrastructure.dataprovider

import fr.realtime.api.user.core.Role
import fr.realtime.api.user.core.RoleName
import fr.realtime.api.user.infrastructure.dataprovider.JpaRole
import fr.realtime.api.user.infrastructure.dataprovider.JpaRoleDao
import fr.realtime.api.user.infrastructure.dataprovider.RoleMapper
import fr.realtime.api.user.infrastructure.dataprovider.RoleRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class JpaRoleDaoTest {
    lateinit var jpaRoleDao: JpaRoleDao

    @Mock
    lateinit var mockRoleRepository: RoleRepository

    @BeforeEach
    fun setup() {
        jpaRoleDao = JpaRoleDao(mockRoleRepository, RoleMapper())
    }

    @Nested
    inner class ExistsByNameTest {
        @Test
        fun `should call roleRepository`() {
            jpaRoleDao.existsByName(RoleName.ROLE_USER)

            verify(mockRoleRepository, times(1)).existsByName(RoleName.ROLE_USER)
        }

        @Test
        fun `when role not exists should return false`() {
            `when`(mockRoleRepository.existsByName(RoleName.ROLE_USER)).thenReturn(false)

            val result = jpaRoleDao.existsByName(RoleName.ROLE_USER)

            assertThat(result).isFalse
        }

        @Test
        fun `when role exists should return true`() {
            `when`(mockRoleRepository.existsByName(RoleName.ROLE_ADMIN)).thenReturn(true)

            val result = jpaRoleDao.existsByName(RoleName.ROLE_ADMIN)

            assertThat(result).isTrue
        }
    }

    @Nested
    inner class SaveTest {
        @Test
        fun `when role saved should return saved role`() {
            val role = Role(
                id = 0,
                name = RoleName.ROLE_ADMIN
            )
            val jpaRoleToSave = JpaRole(
                0,
                RoleName.ROLE_ADMIN
            )
            val jpaSavedRole = JpaRole(
                1,
                RoleName.ROLE_ADMIN
            )
            `when`(mockRoleRepository.save(jpaRoleToSave)).thenReturn(jpaSavedRole)

            val result = jpaRoleDao.save(role)

            val expectedRole = Role(
                jpaSavedRole.id,
                jpaSavedRole.name
            )
            assertThat(result).isEqualTo(expectedRole)
        }
    }
}