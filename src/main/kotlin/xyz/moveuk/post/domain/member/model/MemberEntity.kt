package xyz.moveuk.post.domain.member.model

import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import org.springframework.security.crypto.password.PasswordEncoder
import xyz.moveuk.post.global.entity.BaseEntity

@Entity
@Table(name = "members")
@SQLDelete(sql = "UPDATE members SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
class MemberEntity(
    @Column(name = "email", nullable = false, unique = true)
    val email: String,

    @Transient
    var rawPassword: String,

    @Column(name = "nickname", nullable = false)
    var nickname: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    var role: MemberRole,
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "password", nullable = false)
    var password: String? = null

    fun update(nickname: String, updatedPassword: String) {
        this.nickname = nickname
        this.password = updatedPassword
    }

    fun encodePassword(encoder: PasswordEncoder): MemberEntity {
        this.password = encoder.encode(rawPassword)
        return this
    }
}