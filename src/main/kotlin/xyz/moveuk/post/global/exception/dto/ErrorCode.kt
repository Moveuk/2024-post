package xyz.moveuk.post.global.exception.dto

import org.springframework.http.HttpStatus

interface ErrorCode {
    fun errorName(): String
    fun httpStatus(): HttpStatus
    fun message(): String
    fun code(): String
}

enum class CommonErrorCode(
    private val httpStatus: HttpStatus,
    private val code: String,
    private val message: String
) : ErrorCode {
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "1001", "Invalid parameter included"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "1002", "Resource not exists"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "1003", "Internal server error");

    override fun errorName(): String = this.name
    override fun code(): String = code
    override fun httpStatus(): HttpStatus = httpStatus
    override fun message(): String = message
}

enum class MemberErrorCode(
    private val httpStatus: HttpStatus,
    private val code: String,
    private val message: String
) : ErrorCode {
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "2001", "The password and password verification values do not match"),
    PASSWORD_CANNOT_CONTAIN_NICKNAME(HttpStatus.BAD_REQUEST, "2002", "The password cannot contain nickname"),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "2003", "The email is duplicated");

    override fun errorName(): String = this.name
    override fun code(): String = code
    override fun httpStatus(): HttpStatus = httpStatus
    override fun message(): String = message
}