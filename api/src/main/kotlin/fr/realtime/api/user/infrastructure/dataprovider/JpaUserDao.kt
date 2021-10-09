package fr.realtime.api.user.infrastructure.dataprovider

import fr.realtime.api.user.core.User
import fr.realtime.api.user.core.UserDao
import org.springframework.stereotype.Service

@Service
class JpaUserDao(private val userRepository: UserRepository, private val userMapper: UserMapper) : UserDao {
    override fun findById(userId: Long): User? {
        return userRepository.findById(userId)
                .map { userMapper.entityToDomain(it) }
                .orElse(null)
    }

    override fun existsById(userId: Long): Boolean {
        return userRepository.existsById(userId)
    }

    override fun save(user: User): User {
        val userToSave = userMapper.domainToEntity(user)
        val savedUser = userRepository.save(userToSave)
        return userMapper.entityToDomain(savedUser)
    }
}