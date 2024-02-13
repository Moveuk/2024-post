package xyz.moveuk.post.application.member.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import xyz.moveuk.post.api.member.dto.SignupRequest
import xyz.moveuk.post.domain.member.model.MemberEntity
import xyz.moveuk.post.domain.member.model.MemberRole
import xyz.moveuk.post.domain.member.repository.MemberRepository
import xyz.moveuk.post.global.exception.RestApiException
import xyz.moveuk.post.global.exception.dto.MemberErrorCode
import xyz.moveuk.post.infra.redis.RedisConfig
import xyz.moveuk.post.infra.security.PasswordEncoderConfig
import xyz.moveuk.post.infra.security.jwt.JwtPlugin

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(
    value = [
        JwtPlugin::class,
        RedisConfig::class,
        PasswordEncoderConfig::class]
)
@ActiveProfiles("test")
class MemberServiceDBTest @Autowired constructor(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin
) {

    private val memberService = MemberService(memberRepository, passwordEncoder, jwtPlugin)

    @Test
    fun `비밀번호와 비밀번호 체크 검증 실패시 예외가 발생하는지 확인`() {
        // GIVEN
        val request = SignupRequest(
            email = "sample1@naver.com",
            password = "aaaa",
            passwordCheck = "bbbb",
            nickname = "사용자2"
        )

        // WHEN & THEN
        shouldThrow<RestApiException> {
            memberService.signup(request)
        }.let {
            it.errorCode.errorName() shouldBe MemberErrorCode.PASSWORD_VERIFICATION_MISMATCH.name
            it.errorCode.httpStatus() shouldBe HttpStatus.BAD_REQUEST
            it.errorCode.code() shouldBe "2001"
            it.errorCode.message() shouldBe "The password and password verification values do not match"
        }

        memberRepository.findAll()
            .let {
                it.size shouldBe 0
            }
    }
    @Test
    fun `비밀번호에 닉네임이 들어가면 예외가 발생하는지 확인`() {
        // GIVEN
        val request = SignupRequest(
            email = "sample1@naver.com",
            password = "aaaamember2",
            passwordCheck = "aaaamember2",
            nickname = "member2"
        )

        // WHEN & THEN
        shouldThrow<RestApiException> {
            memberService.signup(request)
        }.let {
            it.errorCode.errorName() shouldBe MemberErrorCode.PASSWORD_CANNOT_CONTAIN_NICKNAME.name
            it.errorCode.httpStatus() shouldBe HttpStatus.BAD_REQUEST
            it.errorCode.code() shouldBe "2002"
            it.errorCode.message() shouldBe "The password cannot contain nickname"
        }

        memberRepository.findAll()
            .let {
                it.size shouldBe 0
            }
    }
    @Test
    fun `이미 회원가입되어있는 이메일이라면 예외가 발생하는지 확인`() {
        // GIVEN
        val 기존_회원 = MemberEntity(
            email = "sample1@naver.com",
            rawPassword = "aaaa",
            nickname = "사용자1",
            role = MemberRole.MEMBER
        ).encodePassword(passwordEncoder)
        memberRepository.saveAndFlush(기존_회원)
        val request = SignupRequest(
            email = "sample1@naver.com",
            password = "aaaa",
            passwordCheck = "aaaa",
            nickname = "사용자2"
        )

        // WHEN & THEN
        shouldThrow<RestApiException> {
            memberService.signup(request)
        }.let {
            it.errorCode.errorName() shouldBe MemberErrorCode.DUPLICATED_EMAIL.name
            it.errorCode.httpStatus() shouldBe HttpStatus.BAD_REQUEST
            it.errorCode.code() shouldBe "2003"
            it.errorCode.message() shouldBe "The email is duplicated"
        }

        memberRepository.findAll()
            .filter { it.email == "sample1@naver.com" }
            .let {
                it.size shouldBe 1
                it[0].nickname shouldBe "사용자1"
            }
    }

    @Test
    fun `정상적으로 회원가입되는 시나리오 확인`() {
        // GIVEN
        val request = SignupRequest(
            email = "sample1@naver.com",
            password = "aaaa",
            passwordCheck = "aaaa",
            nickname = "사용자2"
        )

        // WHEN
        memberService.signup(request)

        // THEN
        memberRepository.findAll()
            .filter { it.email == "sample1@naver.com" }
            .let {
                it.size shouldBe 1
                it[0].nickname shouldBe "사용자2"
            }
    }
}