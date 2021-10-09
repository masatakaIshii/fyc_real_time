package fr.realtime.api.shared.infrastructure.config

import com.google.gson.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.lang.reflect.Type
import java.time.LocalDateTime


@Configuration
class JsonConfig {
    @Bean
    fun getGson(): Gson {
        return GsonBuilder().registerTypeAdapter(LocalDateTime::class.java, object : JsonDeserializer<LocalDateTime?> {
            @Throws(JsonParseException::class)
            override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): LocalDateTime {
                return LocalDateTime.parse(json.asString)
            }
        }).create()
    }
}