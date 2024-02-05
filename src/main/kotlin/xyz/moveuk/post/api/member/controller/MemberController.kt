package xyz.moveuk.post.api.member.controller

import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import xyz.moveuk.post.api.member.dto.LoginRequest
import xyz.moveuk.post.api.member.dto.SignupRequest
import xyz.moveuk.post.application.member.service.MemberService
import xyz.moveuk.post.infra.security.UserPrincipal

@RestController
@RequestMapping("/api/v1/members")
class MemberController(
    private val memberService: MemberService
) {
    @PostMapping("/signup")
    fun signup(@Valid @RequestBody signupRequest: SignupRequest): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.signup(signupRequest))
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody loginRequest: LoginRequest, response: HttpServletResponse): ResponseEntity<String> {
        return memberService.login(loginRequest).let {
            ResponseEntity.status(HttpStatus.OK).header("accessToken", it).build()
        }
    }

    @PreAuthorize("hasRole('MEMBER')")
    @DeleteMapping()
    fun delete(
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(memberService.delete(userPrincipal))
    }

}