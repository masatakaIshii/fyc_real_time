package fr.realtime.api.shared.core.validation

import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import kotlin.reflect.KClass


const val UUID_REGEXP = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$"

/**
 * Validation constraint for {@link UUID}s.
 * Reference: https://stackoverflow.com/questions/37320870/is-there-a-uuid-validator-annotation
 */
@Target(AnnotationTarget.FIELD)
@Constraint(validatedBy = [])
@Retention(AnnotationRetention.RUNTIME)
@NotBlank
@Pattern(regexp = UUID_REGEXP)
annotation class UUID(
        val message: String = "{invalid.uuid}",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)