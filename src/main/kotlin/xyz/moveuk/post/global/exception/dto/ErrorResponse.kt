package xyz.moveuk.post.global.exception.dto

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.validation.FieldError


data class ErrorResponse(
    val httpStatusCode: String,
    val code: String,
    val message: String,
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val errors: List<ValidationError>?
) {

    companion object {
        fun of(errorCode: ErrorCode): ErrorResponse =
            ErrorResponse(errorCode.errorName(), errorCode.code(), errorCode.message(), null)

        fun of(errorCode: ErrorCode, validationErrors: List<ValidationError>): ErrorResponse =
            ErrorResponse(errorCode.errorName(), errorCode.code(), errorCode.message(), validationErrors)
    }

    data class ValidationError(
        val field: String,
        val message: String
    ) {
        companion object {
            fun of(fieldError: FieldError): ValidationError =
                ValidationError(fieldError.field, fieldError.defaultMessage!!)
        }
    }

}