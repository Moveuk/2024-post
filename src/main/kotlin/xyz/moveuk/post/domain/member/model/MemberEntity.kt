package xyz.moveuk.post.domain.member.model

import jakarta.persistence.*
import xyz.moveuk.post.global.entity.BaseTimeEntity

@Entity
@Table(name = "members")
class MemberEntity(
    @Column(name = "email", nullable = false, unique = true)
    val email: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Column(name = "nickname", nullable = false)
    var nickname: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    var role: MemberRole,
) : BaseTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun update(nickname: String, updatedPassword: String) {
        this.nickname = nickname
        this.password = updatedPassword
    }

    companion object {
        fun ofMember(email: String, nickname: String, password: String): MemberEntity = MemberEntity(
            email = email,
            nickname = nickname,
            password = password,
            role = MemberRole.MEMBER,
        )

        fun ofAdmin(email: String, nickname: String, password: String): MemberEntity = MemberEntity(
            email = email,
            nickname = nickname,
            password = password,
            role = MemberRole.ADMIN,
        )
    }
}