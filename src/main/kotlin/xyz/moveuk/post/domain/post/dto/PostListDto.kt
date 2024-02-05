package xyz.moveuk.post.domain.post.dto

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class PostListDto @QueryProjection constructor(
    val postId: Long,
    val title: String,
    val memberId: Long,
    val nickname: String,
    val createdAt: LocalDateTime,
)
