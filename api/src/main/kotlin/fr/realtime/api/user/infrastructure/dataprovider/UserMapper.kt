package fr.realtime.api.user.infrastructure.dataprovider

import fr.realtime.api.shared.core.mapper.Mapper
import fr.realtime.api.user.core.User
import org.springframework.stereotype.Component

@Component
class UserMapper : Mapper<User, JpaUser>{
    override fun entityToDomain(entity: JpaUser): User {
       return User(entity.id, entity.name, entity.password, entity.email)
    }

    override fun domainToEntity(domain: User): JpaUser {
        return JpaUser(
            domain.id,
            domain.name,
            domain.password.orEmpty(),
            domain.email.orEmpty(),
            domain.roles.map { JpaRole(it.id, it.name) }.toSet())
    }

}