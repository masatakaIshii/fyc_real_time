package fr.realtime.api.shared.infrastructure.utils

import fr.realtime.api.shared.core.utils.UUIDHelper
import org.springframework.stereotype.Component
import java.util.*

@Component
class UUIDHelperImpl : UUIDHelper {
    override fun generateRandomUUID(): UUID = UUID.randomUUID()
}