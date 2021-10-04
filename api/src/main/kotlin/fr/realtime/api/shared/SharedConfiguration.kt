package fr.realtime.api.shared

import com.google.gson.Gson
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SharedConfiguration {
    @Bean
    fun createGson(): Gson {
        return Gson()
    }
}