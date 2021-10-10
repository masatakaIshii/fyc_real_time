package fr.realtime.api.theme.core

interface ThemeDao {
    fun save(theme: Theme): Theme
}