package xyz.moveuk.post.domain.post.dto

import com.querydsl.core.annotations.QueryProjection
import xyz.moveuk.post.domain.post.model.Post
import java.time.LocalDateTime

data class PostDto @QueryProjection constructor(
    val postId: Long,
    val title: String,
    val content: String,
    val memberId: Long,
    val nickname: String,
    val images: List<ImageDto>,
    val createdAt: LocalDateTime,
    var hit: Long = 0,
) {
    fun hit(postHit: Long): PostDto {
        this.hit = postHit
        return this
    }

    companion object {
        fun from(post: Post?): PostDto? {
            return if (post != null) {
                PostDto(
                    postId = post.id!!,
                    title = post.title,
                    content = post.content,
                    memberId = post.member.id!!,
                    nickname = post.member.nickname,
                    images = post.images.map { ImageDto.of(it.url) },
                    createdAt = post.createdAt
                )
            } else null
        }
    }
}
