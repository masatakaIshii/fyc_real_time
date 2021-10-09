package fr.realtime.api.shared.infrastructure.utils

import fr.realtime.api.shared.core.utils.PasswordUtils
import org.springframework.stereotype.Component
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

@Component
class PasswordUtilsImpl : PasswordUtils {

    override fun hash(password: String, salt: ByteArray): String {
        val spec = PBEKeySpec(password.toCharArray(), salt, 1000, 256)
        try {
            val skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            return skf.generateSecret(spec).encoded.decodeToString()
        } catch (e: NoSuchAlgorithmException) {
            throw AssertionError("Error while hashing a password: " + e.message, e)
        } catch (e: InvalidKeySpecException) {
            throw AssertionError("Error while hashing a password: " + e.message, e)
        } finally {
            spec.clearPassword()
        }
    }
}