package fr.realtime.api.theme.infrastructure.dataprovider

import org.springframework.data.jpa.repository.JpaRepository

interface ThemeRepository : JpaRepository<JpaTheme, Long> {

}
