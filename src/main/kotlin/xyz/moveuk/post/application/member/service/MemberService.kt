package xyz.moveuk.post.application.member.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import xyz.moveuk.post.api.member.dto.SignupRequest
import xyz.moveuk.post.domain.member.model.MemberEntity
import xyz.moveuk.post.domain.member.repository.MemberRepository
import xyz.moveuk.post.global.exception.RestApiException
import xyz.moveuk.post.global.exception.dto.MemberErrorCode

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    fun signup(signupRequest: SignupRequest): String {
        signupRequest.also {
            // 비밀번호와 비밀번호 체크 검증
            if (it.password != it.passwordCheck) throw RestApiException(MemberErrorCode.PASSWORD_MISMATCH)
        }.also {
            // 비밀번호에 닉네임이 들어가면 예외 처리
            if (it.password.contains(it.nickname)) throw RestApiException(MemberErrorCode.PASSWORD_CANNOT_CONTAIN_NICKNAME)
        }.also {
            // 이메일 중복 확인
            if (memberRepository.existsByEmail(signupRequest.email)) throw RestApiException(MemberErrorCode.DUPLICATED_EMAIL)
        }.also {
            memberRepository.save(
                MemberEntity.ofMember(
                    email = it.email,
                    nickname = it.nickname,
                    password = passwordEncoder.encode(it.password)
                )
            )
        }

        return "회원가입 성공"
    }
}
