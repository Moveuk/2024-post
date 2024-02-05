package xyz.moveuk.post.domain.image.model

import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import xyz.moveuk.post.global.entity.BaseEntity

@Entity
@Table(name = "image")
@SQLDelete(sql = "UPDATE image SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
class Image(
    @Column(name = "title", nullable = false, columnDefinition = "TEXT")
    var url: String
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    companion object {
        fun of(url: String): Image = Image(url)
    }
}