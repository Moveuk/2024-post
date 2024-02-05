package xyz.moveuk.post.api.member.controller

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import xyz.moveuk.post.api.member.dto.SignupRequest
import xyz.moveuk.post.application.member.service.MemberService

@RestController
@RequestMapping("/api/v1/members")
class MemberController(
    private val memberService: MemberService
) {
    @PostMapping("/signup")
    fun signup(@Valid @RequestBody signupRequest: SignupRequest): String {
        return memberService.signup(signupRequest)
    }
    @DeleteMapping()
    fun delete(@RequestParam memberId: Long): String {
        return memberService.delete(memberId)
    }

}