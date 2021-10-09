package fr.realtime.api.shared.infrastructure.utils

import fr.realtime.api.shared.core.utils.DateHelper
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class DateHelperImpl : DateHelper {
    override fun localDateTimeNow(): LocalDateTime = LocalDateTime.now()
}