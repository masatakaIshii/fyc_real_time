package fr.realtime.api.integration.shared.infrastructure.utils

import fr.realtime.api.shared.core.utils.PasswordUtils
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class PasswordUtilsImplTest {
    @Autowired
    lateinit var passwordUtils: PasswordUtils


    @ParameterizedTest
    @ValueSource(strings = ["abcde", "short_password", "a_very_long_long_very_big_and_giant_password"])
    fun `hash when same salt and password should get same hash when call twice`(password: String) {
        val salt = "a salt".toByteArray()
        val firstHash = passwordUtils.hash(password, salt)
        val secondHash = passwordUtils.hash(password, salt)
        assertThat(firstHash).isEqualTo(secondHash)
    }

    @ParameterizedTest
    @ValueSource(strings = ["abcde", "short_password", "a_very_long_long_very_big_and_giant_password"])
    fun `hash when same salt and different password should not get same hash when call twice`(password: String) {
        val salt = "a salt".toByteArray()
        val firstHash = passwordUtils.hash(password, salt)
        val secondHash = passwordUtils.hash("not_same_password", salt)
        assertThat(firstHash).isNotEqualTo(secondHash)
    }

    @ParameterizedTest
    @ValueSource(strings = ["abcde", "short_password", "a_very_long_long_very_big_and_giant_password"])
    fun `hash when different salt and same password should not get same hash when call twice`(password: String) {
        val salt = "a salt".toByteArray()
        val anotherSalt = "another salt".toByteArray()
        val firstHash = passwordUtils.hash(password, salt)
        val secondHash = passwordUtils.hash(password, anotherSalt)
        assertThat(firstHash).isNotEqualTo(secondHash)
    }

    @Test
    fun `hash 'root' and 'root2' should give different hash`() {
        val salt = "a salt".toByteArray()
        val firstHash = passwordUtils.hash("root", salt)
        val secondHash = passwordUtils.hash("root2", salt)
        assertThat(firstHash).isNotEqualTo(secondHash)
    }
}