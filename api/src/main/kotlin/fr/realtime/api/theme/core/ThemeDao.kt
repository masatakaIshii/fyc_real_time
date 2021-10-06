package fr.realtime.api.theme.core

interface ThemeDao {
    fun findLastOneByUsername(username: String): Theme

    fun save(theme: Theme): Theme
}