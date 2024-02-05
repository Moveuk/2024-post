package xyz.moveuk.post.domain.post.model

import jakarta.persistence.*
import org.hibernate.annotations.BatchSize
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import xyz.moveuk.post.domain.image.model.Image
import xyz.moveuk.post.domain.member.model.MemberEntity
import xyz.moveuk.post.global.entity.BaseEntity

@Entity
@Table(name = "post")
@SQLDelete(sql = "UPDATE post SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
class Post(
    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: MemberEntity,

    @BatchSize(size = 50)
    @OneToMany(orphanRemoval = true)
    var images: MutableList<Image> = mutableListOf(),
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun update(title: String, content: String, images: MutableList<Image>) {
        this.title = title
        this.content = content
        this.images = images
    }
}