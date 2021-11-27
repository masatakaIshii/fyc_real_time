package fr.realtime.api.user.infrastructure.dataprovider

import fr.realtime.api.user.core.User
import fr.realtime.api.user.core.UserDao
import org.hibernate.Hibernate
import org.springframework.data.jpa.provider.HibernateUtils
import org.springframework.stereotype.Service

@Service
class JpaUserDao(private val userRepository: UserRepository, private val userMapper: UserMapper) : UserDao {
    override fun findById(userId: Long): User? = userRepository.findById(userId)
        .map(userMapper::entityToDomain)
            .orElse(null)

    override fun existsById(userId: Long): Boolean = userRepository.existsById(userId)

    override fun save(user: User): User {
        val userToSave = userMapper.domainToEntity(user)

        val savedUser = userRepository.save(userToSave)
        return userMapper.entityToDomain(savedUser)
    }

    override fun findByEmail(email: String): User? = userRepository.findByEmail(email)
        .map(userMapper::entityToDomain)
        .orElse(null)

    override fun existsByEmail(email: String): Boolean = userRepository.existsByEmail(email)
}