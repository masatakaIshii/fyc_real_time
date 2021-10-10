package fr.realtime.api.theme.infrastructure.dataprovider

import fr.realtime.api.shared.core.mapper.Mapper
import fr.realtime.api.theme.core.Theme
import org.springframework.stereotype.Component

@Component
class ThemeMapper : Mapper<Theme, JpaTheme> {

    override fun entityToDomain(entity: JpaTheme): Theme = Theme(
            entity.id,
            entity.name,
            entity.username,
            entity.meetingId
    )

    override fun domainToEntity(domain: Theme): JpaTheme = JpaTheme(
            domain.id,
            domain.name,
            domain.username,
            domain.meetingId
    )
}