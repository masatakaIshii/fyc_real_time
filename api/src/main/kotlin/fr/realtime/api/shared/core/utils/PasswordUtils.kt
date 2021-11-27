package fr.realtime.api.shared.core.utils

interface PasswordUtils {
    companion object {
        const val CURRENT_SALT = "prodktober"
    }

    fun hash(password: String, salt: ByteArray): String

    fun encode(password: String): String
}