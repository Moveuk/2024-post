package xyz.moveuk.post.global.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import xyz.moveuk.post.global.exception.dto.CommonErrorCode
import xyz.moveuk.post.global.exception.dto.ErrorCode
import xyz.moveuk.post.global.exception.dto.ErrorResponse


private val kLogger = KotlinLogging.logger {}

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(RestApiException::class)
    fun handleRestApiException(e: RestApiException): ResponseEntity<ErrorResponse> {
        kLogger.warn { "${e.errorCode.code()}: ${e.errorCode.message()}" }
        return handleExceptionInternal(e.errorCode)
    }

    override fun handleMethodArgumentNotValid(
        e: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val errorCode = CommonErrorCode.INVALID_PARAMETER
        kLogger.warn { "${errorCode.code()}: ${errorCode.message()}" }
        return handleExceptionInternal(errorCode, e)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(e: AccessDeniedException): ResponseEntity<ErrorResponse> {
        kLogger.warn { "${CommonErrorCode.UNAUTHORIZED.code()}: ${CommonErrorCode.UNAUTHORIZED.message()}" }
        return handleExceptionInternal(CommonErrorCode.UNAUTHORIZED)
    }

    @ExceptionHandler(Exception::class)
    fun handleAllException(e: Exception?): ResponseEntity<ErrorResponse> {
        kLogger.error(e) { "GlobalExceptionHandler.handleAllException: $e" }
        val errorCode: ErrorCode = CommonErrorCode.INTERNAL_SERVER_ERROR
        return handleExceptionInternal(errorCode)
    }

    private fun handleExceptionInternal(errorCode: ErrorCode): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(errorCode.httpStatus())
            .body(ErrorResponse.of(errorCode))
    }

    private fun handleExceptionInternal(errorCode: ErrorCode, e: BindException): ResponseEntity<Any> {
        return ResponseEntity.status(errorCode.httpStatus())
            .body(makeErrorResponse(errorCode, e))
    }

    private fun makeErrorResponse(errorCode: ErrorCode, e: BindException): ErrorResponse {
        return e.bindingResult
            .fieldErrors
            .map(ErrorResponse.ValidationError::of).let {
                ErrorResponse.of(errorCode, it)
            }
    }

}
