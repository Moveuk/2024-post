package xyz.moveuk.post.api.valid

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [PasswordValidator::class])
@Target(allowedTargets = [FIELD])
@Retention(AnnotationRetention.RUNTIME)
annotation class Password(
    val message: String = "Password is not allow",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)