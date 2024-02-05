package xyz.moveuk.post.api.member.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.springframework.validation.annotation.Validated
import xyz.moveuk.post.api.valid.Password

@Validated
data class LoginRequest(
    @field: Email(message = "올바른 이메일 형식이 아닙니다.")
    @field: NotBlank(message = "이메일을 입력해주세요")
    val email: String,
    @field: Password
    val password: String,
)