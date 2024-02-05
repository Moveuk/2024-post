package xyz.moveuk.post.api.member.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.springframework.validation.annotation.Validated
import xyz.moveuk.post.api.valid.Password
import xyz.moveuk.post.domain.member.model.MemberEntity
import xyz.moveuk.post.domain.member.model.MemberRole

@Validated
data class SignupRequest(
    @field: Email(message = "올바른 이메일 형식이 아닙니다.")
    @field: NotBlank(message = "이메일을 입력해주세요")
    val email: String,
    @field: Password
    val password: String,
    @field: Password
    val passwordCheck: String,
    @field: NotBlank(message = "닉네임을 입력해주세요")
    val nickname: String
) {
    fun toMemberEntity(): MemberEntity = MemberEntity(
        email = email,
        nickname = nickname,
        rawPassword = password,
        role = MemberRole.MEMBER
    )
}
