package xyz.moveuk.post.application.member.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import xyz.moveuk.post.api.member.dto.LoginRequest
import xyz.moveuk.post.api.member.dto.SignupRequest
import xyz.moveuk.post.domain.member.repository.MemberRepository
import xyz.moveuk.post.global.exception.RestApiException
import xyz.moveuk.post.global.exception.dto.CommonErrorCode
import xyz.moveuk.post.global.exception.dto.MemberErrorCode
import xyz.moveuk.post.infra.security.UserPrincipal
import xyz.moveuk.post.infra.security.jwt.JwtPlugin

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin,
) {
    fun signup(signupRequest: SignupRequest): String {
        // 비밀번호와 비밀번호 체크 검증 check assert
        check(signupRequest.password == signupRequest.passwordCheck) { throw RestApiException(MemberErrorCode.PASSWORD_VERIFICATION_MISMATCH) }
        // 비밀번호에 닉네임이 들어가면 예외 처리
        check(!signupRequest.password.contains(signupRequest.nickname)) { throw RestApiException(MemberErrorCode.PASSWORD_CANNOT_CONTAIN_NICKNAME) }
        // 이메일 중복 확인
        check(!memberRepository.existsByEmail(signupRequest.email)) { throw RestApiException(MemberErrorCode.DUPLICATED_EMAIL) }

        signupRequest.toMemberEntity()
            .encodePassword(passwordEncoder)
            .let { memberRepository.save(it) }
        return "회원가입 성공"
    }

    fun login(loginRequest: LoginRequest): String {
        val findMember = memberRepository.findByEmail(loginRequest.email) ?: throw RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND)

        check(passwordEncoder.matches(loginRequest.password, findMember.password)) { throw RestApiException(MemberErrorCode.PASSWORD_MISMATCH) }

        return jwtPlugin.generateAccessToken(
                subject = findMember.id.toString(),
                email = loginRequest.email,
                role = findMember.role.name
        )
    }

    fun delete(userPrincipal: UserPrincipal): String {
        val findMember = memberRepository.findByIdOrNull(userPrincipal.id) ?: throw RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND)
        memberRepository.delete(findMember)

        return "회원 탈퇴 성공"
    }
}
