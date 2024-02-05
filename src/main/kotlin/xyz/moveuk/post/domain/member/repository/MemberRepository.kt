package xyz.moveuk.post.domain.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import xyz.moveuk.post.domain.member.model.MemberEntity

interface MemberRepository : JpaRepository<MemberEntity, Long> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): MemberEntity?
}
