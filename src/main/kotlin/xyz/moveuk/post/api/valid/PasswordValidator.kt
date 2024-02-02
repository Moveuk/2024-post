package xyz.moveuk.post.api.valid

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.stereotype.Component
import java.text.MessageFormat
import java.util.regex.Pattern

@Component
class PasswordValidator : ConstraintValidator<Password, String> {
    override fun initialize(constraintAnnotation: Password) {
    }

    override fun isValid(password: String, context: ConstraintValidatorContext): Boolean {
        if (!matches(password)) {
            context.disableDefaultConstraintViolation()
            context.buildConstraintViolationWithTemplate(
                MessageFormat.format("{0}자 이상의 {1}자 이하의 숫자, 영문자, 특수문자를 포함한 비밀번호를 입력해주세요", MIN_SIZE, MAX_SIZE)
            )
                .addConstraintViolation()
            return false
        }
        return true
    }

    fun isValid(password: String): Boolean {
        return password.trim().isNotEmpty() && matches(password)
    }

    fun matches(password: String): Boolean {
        return PATTERN.matcher(password).matches()
    }

    companion object {
        private const val MIN_SIZE = 4
        private const val MAX_SIZE = 20
        private const val regexPassword =
            ("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]" +
                    "{" + MIN_SIZE + "," + MAX_SIZE + "}$")
        private val PATTERN: Pattern = Pattern.compile(regexPassword)
    }
}