package xyz.moveuk.post.application.member.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import xyz.moveuk.post.api.member.dto.SignupRequest
import xyz.moveuk.post.domain.member.repository.MemberRepository
import xyz.moveuk.post.global.exception.RestApiException
import xyz.moveuk.post.global.exception.dto.MemberErrorCode

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    fun signup(signupRequest: SignupRequest): String {
        // 비밀번호와 비밀번호 체크 검증 check assert
        check(signupRequest.password == signupRequest.passwordCheck) { throw RestApiException(MemberErrorCode.PASSWORD_MISMATCH) }
        // 비밀번호에 닉네임이 들어가면 예외 처리
        check(!signupRequest.password.contains(signupRequest.nickname)) { throw RestApiException(MemberErrorCode.PASSWORD_CANNOT_CONTAIN_NICKNAME) }
        // 이메일 중복 확인
        check(!memberRepository.existsByEmail(signupRequest.email)) { throw RestApiException(MemberErrorCode.DUPLICATED_EMAIL) }

        signupRequest.toMemberEntity()
            .encodePassword(passwordEncoder)
            .let { memberRepository.save(it) }
        return "회원가입 성공"
    }

    fun delete(memberId: Long): String {
        val findMember = memberRepository.findByIdOrNull(memberId)
        findMember ?: throw RuntimeException()
        memberRepository.delete(findMember)

        return "회원 탈퇴 성공"
    }
}
